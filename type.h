#ifndef TYPE_H
#define TYPE_H
#include "base_type.h"
#define MATERIAL_SHIFT 1
#define SHAPE_SHIFT 99
#define SIZE_SHIFT 3
#define GET_TYPE_INDEX(i1,i2,i3) ((i1)+(i2)*99+(i3)*3)
#define SET_TYPE_INDEX(i,i1,i2,i3) i1=(i)%3;i2=(i)/99;i3=((i)/3)%33

/**
 * TODO more comment
 */
typedef struct {
//original index
 Byte index;
 Byte index2;

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
} TypeB;

int setupTypes(TypeB* types,const char* buf);
Bool ifPassType(const TypeB* type,Double compression,Double tension,Double length,Double slenderness);
Double getCompressionStrength(const TypeB* type,Double length);
char* type_print(const TypeB* type);


#endif
