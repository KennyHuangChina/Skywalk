LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := communic
LOCAL_CFLAGS := -DDEBUG_INFO
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_LDLIBS := \
	-llog \

LOCAL_SRC_FILES := \
	NativeCall.cpp \

include $(BUILD_SHARED_LIBRARY)
