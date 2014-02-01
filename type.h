#ifndef TYPE_H
#define TYPE_H
#include "base_type.h"

/**
 * TODO more comment
 */
struct TypeB{
 Byte materialIndex;
 Byte shapeIndex;
 Byte sizeIndex;

 char name[6];

//load
 Double weight;
//stiffness
 Double AE;
//cost
 Double cost;
//strength
 Double tensionStrength;
//slenderness
 Double inverseRadiusOfGyration;
//
 Double FyArea_d_CEMoment;
//
 Double FyArea;
};

int setupTypes(TypeB* types,const char* buf);
bool ifPassType(TypeB* type,Double compression,Double tension,Double length,Double slenderness);
Double getCompressionStrength(const TypeB* type,Double length);


#endif
