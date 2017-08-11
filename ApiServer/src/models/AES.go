package models

import (
	"bytes"
	"crypto/aes"
	"crypto/cipher"
	"crypto/md5"
	"errors"
	"fmt"
	"github.com/astaxie/beego"
)

////////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- Functions for encrypting and decrypting --
//
func makeValidityKey(track_id, rand string) ([]byte, error) {
	FN := "[makeValidityKey] "

	if 0 == len(track_id) {
		return nil, errors.New("no track_id assigned")
	}
	if 0 == len(rand) {
		return nil, errors.New("no rand assigned")
	}
	beego.Debug(FN, "track_id:", track_id, ", rand:", rand)

	// key1 := track_id + rand
	// key := make([]byte, 0)
	// for _, v := range key1 {
	// 	key = append(key, byte(v))
	// }

	md5 := md5.Sum([]byte(track_id + rand))
	beego.Debug(FN, "key:", track_id+rand)
	beego.Debug(FN, "md5:", md5)

	k := ""
	for i := 0; i < 8; i++ {
		k = k + fmt.Sprintf("%.2x", md5[i])
	}
	// k := "5611f21af6d59aa2"
	beego.Debug(FN, "tmp key:", k, ", len:", len(k))
	return []byte(k), nil

	// key := make([]byte, 16)
	// for k, v := range md5 {
	// 	key[k] = v
	// }
	// beego.Debug(FN, "key:", key, "(", string(key), ")")

	// return key, nil
}

func EncryptString(plaintext, track_id, salt string) (ciphertxt string, err error) {
	FN := "[EncryptString] "

	if 0 == len(plaintext) {
		return "", errors.New("no plaintext to encrypt")
	}
	key, err := makeValidityKey(track_id, salt)
	if nil != err {
		return "", err
	}
	beego.Debug(FN, "key:", key, ", len:", len(key))

	ciphertext := AesEncrypt([]byte(plaintext), key)
	if 0 == len(ciphertext) {
		return "", errors.New("Fail to encrypt the plaintext specified")
	}
	beego.Debug(FN, "ciphertext:", ciphertext)
	beego.Debug(FN, "ciphertext:", string(ciphertext))

	return string(ciphertext), nil
}

func DecryptString(ciphertxt, track_id, rand string) (plaintext string, err error) {
	FN := "[DecryptString] "
	beego.Info(FN, "ciphertxt:", ciphertxt, ", track_id:", track_id, ", rand:", rand)

	if 0 == len(ciphertxt) {
		return "", errors.New("no ciphertxt to decrypt")
	}
	key, err := makeValidityKey(track_id, rand)
	if nil != err {
		return "", err
	}
	beego.Debug(FN, "key:", key, ", len:", len(key))

	plaintext1, err := AesDecrypt([]byte(ciphertxt), key)
	beego.Debug(FN, "plaintext1:", plaintext1, ", err:", err)
	beego.Debug(FN, "plaintext1:", string(plaintext1))

	return string(plaintext1), nil
}

func DecryptBuff(ciphertxt []byte, track_id, rand string) (plaintext string, err error) {
	FN := "[DecryptBuff] "
	beego.Info(FN, "ciphertxt:", ciphertxt, ", track_id:", track_id, ", rand:", rand)

	if 0 == len(ciphertxt) {
		return "", errors.New("no ciphertxt to decrypt")
	}
	key, err := makeValidityKey(track_id, rand)
	if nil != err {
		return "", err
	}
	beego.Debug(FN, "key:", key, ", len:", len(key))

	plaintext1, err := AesDecrypt(ciphertxt, key)
	beego.Debug(FN, "plaintext1:", plaintext1, ", err:", err)
	beego.Debug(FN, "plaintext1:", string(plaintext1))

	return string(plaintext1), nil
}

func AesEncrypt(plaintext, key []byte) []byte {
	// FN := "[AesEncrypt] "

	block, err := aes.NewCipher(key)
	if err != nil {
		beego.Error("key error1: ", err)
		return nil
	}
	if 0 == len(plaintext) {
		fmt.Println("plain content empty")
		return nil
	}
	// beego.Debug(FN, "aes.BlockSize:", aes.BlockSize)
	// beego.Debug(FN, "BlockSize:", block.BlockSize())

	blockModel := cipher.NewCBCEncrypter(block, key)
	plaintext = []byte(plaintext)
	plaintext = PKCS5Padding(plaintext, block.BlockSize())
	crypted := make([]byte, len(plaintext))
	blockModel.CryptBlocks(crypted, plaintext)
	// beego.Debug(FN, "crypted:", crypted)
	// beego.Debug(FN, "crypted:", string(crypted))
	// beego.Debug(FN, "crypted:", base64.StdEncoding.EncodeToString(crypted))

	return crypted
}

func AesDecrypt(ciphertext, key []byte) ([]byte, error) {
	FN := "[AesDecrypt] "
	beego.Debug(FN, "ciphertext:", ciphertext, "(", len(ciphertext), "), Key:", key)

	keyBytes := []byte(key)
	beego.Debug(FN, "keyBytes:", keyBytes)

	block, err := aes.NewCipher(keyBytes) //选择加密算法. AES128/196/256
	if err != nil {
		return nil, err
	}
	blockSize := block.BlockSize()
	beego.Debug(FN, "blockSize:", blockSize)
	blockModel := cipher.NewCBCDecrypter(block, keyBytes)
	// beego.Debug(FN, "blockModel:", blockModel)

	plantText := make([]byte, len(ciphertext))
	// beego.Debug(FN, "plantText:", plantText)

	blockModel.CryptBlocks(plantText, ciphertext)
	// beego.Debug(FN, "CryptBlocks")
	plantText = PKCS5UnPadding(plantText, block.BlockSize())
	// beego.Debug(FN, "plantText:", plantText)

	return plantText, nil
}

func PKCS5Padding(ciphertext []byte, blockSize int) []byte {
	padding := blockSize - len(ciphertext)%blockSize
	padtext := bytes.Repeat([]byte{byte(padding)}, padding)
	return append(ciphertext, padtext...)
}

func PKCS5UnPadding(plantText []byte, blockSize int) []byte {
	length := len(plantText)
	unpadding := int(plantText[length-1])
	return plantText[:(length - unpadding)]
}
