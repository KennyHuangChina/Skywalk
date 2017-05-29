LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := communic
LOCAL_CFLAGS := -DDEBUG_INFO -DANDROID
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_LDLIBS := \
	-llog \

LOCAL_SRC_FILES := \
	NativeCall.cpp \
	SkMD5Abstract.cpp \
	SkMD5Impl.cpp \
	DP.cpp \
	DigUtil.cpp \
	JniUtil.cpp \

include $(BUILD_SHARED_LIBRARY)
