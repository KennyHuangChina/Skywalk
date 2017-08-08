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
func makeValidityKey(track_id, salt string) ([]byte, error) {
	FN := "[makeValidityKey] "

	if 0 == len(track_id) {
		return nil, errors.New("no track_id assigned")
	}
	if 0 == len(salt) {
		return nil, errors.New("no salt assigned")
	}
	// beego.Debug(FN, "plaintext:", plaintext, ", track_id:", track_id, ", salt:", salt)

	md5 := md5.Sum([]byte("cidana" + track_id + salt + "2017"))
	beego.Debug(FN, "key:", "cidana"+track_id+salt+"2017")
	beego.Debug(FN, "md5:", md5)

	key := make([]byte, 16)
	for k, v := range md5 {
		key[k] = v
	}

	return key, nil
}

func EncryptString(plaintext, track_id, salt string) (ciphertxt string, err error) {
	// FN := "[EncryptString] "

	if 0 == len(plaintext) {
		return "", errors.New("no plaintext to encrypt")
	}
	key, err := makeValidityKey(track_id, salt)
	if nil != err {
		return "", err
	}
	// beego.Debug(FN, "key:", key, ", len:", len(key))

	ciphertext := AesEncrypt([]byte(plaintext), key)
	if 0 == len(ciphertext) {
		return "", errors.New("Fail to encrypt the plaintext specified")
	}
	// beego.Debug(FN, "ciphertext:", ciphertext)
	// beego.Debug(FN, "ciphertext:", string(ciphertext))

	return string(ciphertext), nil
}

func DecryptString(ciphertxt, track_id, salt string) (plaintext string, err error) {
	// FN := "[EncryptString] "

	if 0 == len(ciphertxt) {
		return "", errors.New("no ciphertxt to decrypt")
	}
	key, err := makeValidityKey(track_id, salt)
	if nil != err {
		return "", err
	}
	// beego.Debug(FN, "key:", key, ", len:", len(key))

	plaintext1, err := AesDecrypt([]byte(ciphertxt), key)
	// beego.Debug(FN, "plaintext1:", plaintext1, ", err:", err)
	// beego.Debug(FN, "plaintext1:", string(plaintext1))

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
	keyBytes := []byte(key)
	block, err := aes.NewCipher(keyBytes) //选择加密算法
	if err != nil {
		return nil, err
	}
	blockModel := cipher.NewCBCDecrypter(block, keyBytes)
	plantText := make([]byte, len(ciphertext))
	blockModel.CryptBlocks(plantText, ciphertext)
	plantText = PKCS5UnPadding(plantText, block.BlockSize())
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
