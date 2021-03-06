
#include "optimizer.h"
#define eprintf(...) printf(__VA_ARGS__)

static char spacebuf[]="          ";
int optimize(TypeHintCostB* f,const OptimizeTask* task){
    int memberSize=task->memberSize;
    int typeSize=task->typeSize;
    //used as final return
    //index of type with minCost for given member
    Byte memberMinIndex[MAX_MEMBER];

    //used for recursion

    //bundle[n+1]=typeIndex for bundle n;
    Byte bundle[MAX_BUNDLE+2];
    //bundleLength[n] is the remaining totalLength after having first n bundles
    Double bundleLength[MAX_BUNDLE+1];
    //bundleCost[n] is the totalCost after having first n bundles
    Dollar bundleCost[MAX_BUNDLE+1];
    //alternative to memberBundleIndex
    TestMask bundleRemain[MAX_BUNDLE+1];
    Dollar bundleMinCost[MAX_BUNDLE+1];

    f->cost=task->capCost;
   
    //update bundleLength[0]
    bundleLength[0]=0;
    int t;
    for(t=0;t<memberSize;++t){
     bundleLength[0]+=task->length[t];
    }
   
    //update memberMinIndex, bundleMinCost[0]
    bundleMinCost[0]=0;
    bundleRemain[0]=(((TestMask)1)<<memberSize)-1;
    for(t=0;t<typeSize;++t){
        TestMask valid=bundleRemain[0]&task->typeTestMask[t];
        bundleRemain[0]^=valid;
        int tmp=0;
        while(valid!=0){
            if(valid&1){
                memberMinIndex[tmp]=t;
                bundleMinCost[0]+=task->length[tmp]*task->cost[t];
            }
            valid>>=1;
            ++tmp;
        }
    }
   
    bundleRemain[0]=(((TestMask)1)<<memberSize)-1;
    bundleCost[0]=0;
    bundle[0]=0;
    bundle[1]=0;
    
   
    int level=0;
    int count[8];
    for(t=0;t<8;t++){
        count[t]=0;
    }
    while(level>-1){
        //too many bundle
        if(level>=MAX_BUNDLE){
            count[4]++;
            --level;
            continue;
        }

        //tested all types
        if(bundle[level+1]==typeSize-1){
            count[5]++;
            --level;
            continue;
        }
        ++level;
        ++bundle[level];
        TestMask valid=bundleRemain[level-1]&task->typeTestMask[bundle[level]];
        if(valid==0){
            count[6]++;
            --level;
            continue;
        }
        count[0]++;
        bundleLength[level]=bundleLength[level-1];
        bundleCost[level]=bundleCost[level-1]+task->bundleCost;
        bundleMinCost[level]=bundleMinCost[level-1];
        bundleRemain[level]=bundleRemain[level-1]^valid;
        if(level<MAX_BUNDLE){
            bundle[level+1]=bundle[level]+1;
        }
        //TODO optimize performance, take advantage of bitset.
        while(valid!=0){
            //TODO
            int tt=FFSLLL(valid)-1;
            bundleLength[level] -= task->length[tt];
            bundleCost[level] +=task->cost[bundle[level]]*task->length[tt];
            bundleMinCost[level]-=task->cost[memberMinIndex[tt]]*task->length[tt];
            valid^=((TestMask)1)<<tt;
        }
        //eprintf("%s%d:%5lf|%5lf|%5lf|%5lf|%s",spacebuf+(10-level),level,bundleLength[level],bundleCost[level],bundleMinCost[level],print_bytes(&bundleRemain[level],8));
        //stop when cost too large
        if(bundleCost[level] + bundleMinCost[level] > f->cost){
            count[2]++;
            --level;
            continue;
        }
        valid=task->typeTestMask[bundle[level]];
        for(t=level-1;t>0;t--){
            if((valid|(bundleRemain[t]^bundleRemain[t-1]))==valid&&(bundleLength[t-1]-bundleLength[t])*(task->cost[bundle[level]]-task->cost[bundle[t]])<task->bundleCost){
                count[7]++;
                --level;
                continue;
            }
        }

        //finished?
        if(bundleRemain[level]==0){
            count[1]++;
            //new best?
            if(bundleCost[level] < f->cost){
                //copy to f
                f->cost=bundleCost[level];
                for(t=0;t<level;++t){
                    f->bundle[t]=task->index[bundle[t+1]];
                }
                for(t=0;t<memberSize;++t){
                    TestMask tmpMask=((TestMask)(1))<<t;
                    Byte tmp=level-1;
                    while(TRUE){
                        if(task->typeTestMask[bundle[tmp+1]]&bundleRemain[tmp]&(tmpMask)){
                            f->member[t]=tmp;
                            break;
                        }
                        --tmp;
                    }
                }
            }
            level-=2;
            continue;
        }
        //stop when bundle total length too small
        if(level>0 && (bundleLength[level-1] - bundleLength[level] <task->minLength)){
            count[3]++;
            --level;
            continue;
        }
    }
    for(t=0;t<8;t++){
        g_debug("count %d %d",t,count[t]);
    }
    return 0;
}
