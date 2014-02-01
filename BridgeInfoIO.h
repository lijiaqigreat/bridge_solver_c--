
#include <stdlib.h>
#include <string.h>
#include "BridgeInfo.h"
#include "rc4.h"
#include "type.h"


#define BUFLEN 100000
#define TYPE_PATH "sample2.txt"


const BridgeInfo* loadBridge(const char* path);
