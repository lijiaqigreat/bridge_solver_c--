#include "type.h"
#include <string.h>
#include <math.h>
int setupTypes(TypeB* types,const char* buf){
  for(int t=0;t<MAX_TYPE;t++){
    int i1,i2,i3;
    Double E,Fy,Density,Area,Moment,CostVol;
    char name[6];
    int n=sscanf(buf,"%d %d %d %5s %lf %lf %lf %lf %lf %lf\n",&i1,&i2,&i3,name,&E,&Fy,&Density,&Area,&Moment,&CostVol);
    while(*(buf++)!='\n'){}
    if(n!=10){
      return 1;
    }
    types[t].materialIndex=(Byte)i1;
    types[t].shapeIndex=(Byte)i2;
    types[t].sizeIndex=(Byte)i3;
    strcpy(types[t].name,name);
    types[t].weight=DEAD_LOAD_FACTOR*Density*Area*GRAVITY/2000;
    types[t].AE=Area*E;
    types[t].cost=CostVol*Area*Density*Area*GRAVITY*2;
    types[t].tensionStrength=TENSION_RESISTANCE_FACTOR*Fy*Area;
    types[t].inverseRadiusOfGyration=sqrt(Area/Moment);
    types[t].FyArea_d_CEMoment=Fy*Area*(PI2*E*Moment);
    types[t].FyArea=Fy*Area;
  }
  return 0;
}

bool ifPassType(const TypeB* type,Double compression,Double tension,Double length,Double slenderness){
  return (tension <= type->tensionStrength)&&
         (slenderness>length*type->inverseRadiusOfGyration)&&
         (compression <= getCompressionStrength(type,length));
}
Double getCompressionStrength(const TypeB* type,Double length){
  Double lambda=length*length*type->FyArea_d_CEMoment;
  return lambda<=2.25?
           COMPRESSION_RESISTANCE_FACTOR*pow(0.66,lambda)*type->FyArea:
           0.792*type->FyArea/lambda;
}


