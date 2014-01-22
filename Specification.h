#ifndef SPECIFICATIONB_H
#define SPECIFICATIONB_H
#include "base_type.h"

struct DeckType{
 char name[16];
 Double cost;
 Double weight;
};
struct TruckType{
 char name[16];
 Double frontRaw;
 Double backRaw;
};
struct TypeB{
 char name[16];
 Double e;
 Double fy;
 Double density;
 Double area;
 Double moment;
 Double cost_vol;
};
struct SpecificationB{
 Double compressionResistanceFactor;
 Double tensionResistanceFactor;
 Double gravity;
 Double deadLoadFactor;
 Double liveLoadFactor;
 Double c;
 Double bundleCost;
 Double jointSingleCost;
 Int deckTypeSize;
 DeckTypeB deckTypes[MAX_DECK_TYPE];
 TruckTypeB truckTypes[MAX_TRUCK_TYPE];
 TypeB types[MAX_TYPE];
}


#endif
