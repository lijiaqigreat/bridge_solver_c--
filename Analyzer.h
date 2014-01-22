#ifndef ANALYZER_H
#define ANALYZER_H

#include "BridgeInfo.h"

/**
 * Analyzer represent a work unit that can solve bridges.
 * It can compute full result (in result),
 *        compute optimizeTask.
 * WARNING: cannot run parallelly
 */
class Analyzer{
 BridgeInfo bridgeInfo;
 PositionHintB positionHintB;
 TypeHintCostB typeHintCostB;
 Result result;
 OptimizeTask optimizeTask;
 
 /**
  * change bridgeInfo
  */
 void load(BridgeInfo _bridgeInfo);
 void load(const PositionHintB& _positionHint, const TypeHintCostB& _typeHintCostB);
 void analyze();
}


#endif
