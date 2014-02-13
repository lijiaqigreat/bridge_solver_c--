#ifndef TEST_MASK_H 
#define TEST_MASK_H 

#include "base_type.h"
#define 

#define mask_or(a,b) (a).a|=(b).a;(a).b|=(b).b
#define mask_and(a,b) (a).a&=(b).a;(a).b&=(b).b
#define mask_xor(a,b) (a).a^=(b).a;(a).b^=(b).b
#define mask_ones(a,n) if((n)>=64){(a).a=(guint64)-1L;(a).b=(1L<<(n))-1;}else{(a).a=(1L<<(n))-1;(a).b=0L;}
#define mask_one(a,n) if((n)>=64){(a).a=0L;(a).b=1L<<(n);}else{(a).a=1L<<(n);(a).b=0L;}
#define mask_ffs(a) ((a).a==0?FFSLL((a).b)+64:FFSLL((a).a))




#endif
