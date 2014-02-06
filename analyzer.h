#ifndef ANALYZER_H
#define ANALYZER_H

#include "BridgeInfo.h"
#include "Result.h"
#include "type.h"

/**
 * Analyzer represent a work unit that can solve bridges.
 * It can compute full result (in result),
 *        compute optimizeTask.
 * WARNING: cannot run parallelly
 */
int analyze(Result* result,OptimizeTask* task,const BridgeInfo *bridgeInfo,gconstpointer element);


#endif
