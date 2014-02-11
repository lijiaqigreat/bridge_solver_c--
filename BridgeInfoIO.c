
#include "BridgeInfoIO.h"
#include "rc4.h"
#include <string.h>

guchar* readFile(const gchar* path,Bool encrypt){
    GError* error=NULL;
    guchar *buf=NULL;
    gsize size;
    g_file_get_contents(path,&buf,&size,&error);
    if (error) {
        g_critical("ERROR: %s\n",error->message);
        g_error_free(error);
        return 0;
    }
    if(encrypt){
        endecrypt_rc4(buf,(int)size);
    }
    return buf;
}
int writeFile(const gchar* path, const guchar* buf,Bool encrypt){
    //TODO better performance by storing length somewhere?
    gsize size=strlen(buf);
    GError *error=NULL;
    if(encrypt){
        buf=strndup(buf,size);
        endecrypt_rc4(buf,(int)size);
        g_file_set_contents(path,buf,size,error);
        free(buf);
    }else{
        g_file_set_contents(path,buf,size,error);
    }
    if(error!=NULL){
        g_critical("ERROR: %s\n",error->message);
        g_error_free(error);
        return 2;
    }
    return 0;
}

int findIndex(const TypeB *types,int index){
    int t;
    for(t=0;t<MAX_TYPE;t++){
        if(types[t].index==index){
            return t;
        }
    }
    return -1;
}
const BridgeInfo* loadBridge(const gchar* path,const gchar* type_path){
    /* encrypt buffer */
    BridgeInfo* f=(BridgeInfo*) malloc(sizeof(BridgeInfo));
    gchar* buf=(gchar*) readFile(path,TRUE);
    f->buf=buf;
    printf("decrypted test:\n%s\n",buf);
    int year;
    int jointSize;
    int memberSize;
    char code[10];
    unsigned long long conditionID;
    {
        char jss[3],mss[4];
        sscanf(buf,"%4d%10llu%2[ 0-9]%3[ 0-9]",&year,&conditionID,jss,mss);
        sscanf(jss,"%d",&jointSize);
        sscanf(mss,"%d",&memberSize);
    }
    int t;
    for(t=0;t<10;t++){
        code[9-t]=conditionID%10;
        conditionID/=10;
    }
    char* buf2=(char*) readFile(type_path,FALSE);
    setupTypes((TypeB*)(&f->types[0]),buf2);
    f->totalJointSize=jointSize;
    f->memberSize=memberSize;
    f->bundleCost=BUNDLE_COST;
    Bool hiPier=code[9]>0;
    int pierPanelIndex=code[8]-1;
    int pierJointIndex=code[8]-1;
    Bool pier=pierPanelIndex>=0;
    Bool arch=code[7]==1;
    Bool leftCable=code[7]==2||code[7]==3;
    Bool rightCable=code[7]==3;
    int underClearance=10*code[5]+code[6];
    int overClearance=10*code[3]+code[4];
    int nPanels=10*code[1]+code[2];
    int loadCaseIndex=code[0]-1;
    int loadType=(loadCaseIndex&1)==0?0:1;
    int deckType=(loadCaseIndex&2)==0?0:1;
    int deckElevation;
    int archHeight;
    if(arch){
        deckElevation = (4 * (nPanels - 5) + underClearance);
        archHeight = underClearance;
    }else{
        deckElevation = (4 * (nPanels - 5));
        archHeight = -1;
    }
    int overMargin = (32 - deckElevation);
    int pierHeight = (pier ? deckElevation - underClearance : hiPier ? deckElevation : -1);
    int nPrescribedJoints = nPanels + 1;
    int archJointIndex=-1;
    int leftAnchorageJointIndex=-1;
    int rightAnchorageJointIndex=-1;
    if ((pier) && (!hiPier)){
        pierJointIndex = nPrescribedJoints;
        nPrescribedJoints += 1;
    }
    if (arch)
    {
        archJointIndex = nPrescribedJoints;
        nPrescribedJoints += 2;
    }
    int nAnchorages = 0;
    if (leftCable)
    {
        leftAnchorageJointIndex = nPrescribedJoints;
        nAnchorages += 1;
        nPrescribedJoints += 1;
    }
    if (rightCable)
    {
        //assert (leftCable);
        rightAnchorageJointIndex = nPrescribedJoints;
        nAnchorages += 1;
        nPrescribedJoints += 1;
    }
    int spanLength = (nPanels * 4.0D);
    int nLoadedJoints = (nPanels + 1);



    f->fixedJointSize=nPrescribedJoints;
    f->deckSize=nPanels;
    f->deckWeight= deckType == 0 ? 183.42825000000002 : 136.357;

    f->backWeight=loadType==0?181.0:120.0;
    f->frontWeight=loadType==0?44.0:120.0;
    f->backWeight*=LIVE_LOAD_FACTOR;
    f->frontWeight*=LIVE_LOAD_FACTOR;

    f->slenderness=(leftCable||rightCable)?1e100:300;
    {
        Bool restraint[MAX_FIXED_JOINT*2];
        for(t=0;t<MAX_FIXED_JOINT*2;t++){
            restraint[t]=FALSE;
        }
        restraint[0]=TRUE;
        restraint[1]=TRUE;
        restraint[nLoadedJoints*2-1]=TRUE;
        if(pier){
            restraint[pierJointIndex*2]=TRUE;
            restraint[pierJointIndex*2+1]=TRUE;
            if(hiPier){
                restraint[0]=FALSE;
            }
        }
        if(arch){
            restraint[nLoadedJoints*2-1]=FALSE;
            restraint[0]=FALSE;
            restraint[1]=FALSE;
            restraint[archJointIndex*2+0]=TRUE;
            restraint[archJointIndex*2+1]=TRUE;
            restraint[archJointIndex*2+2]=TRUE;
            restraint[archJointIndex*2+3]=TRUE;
        }
        if(leftCable){
            restraint[leftAnchorageJointIndex*2]=TRUE;
            restraint[leftAnchorageJointIndex*2+1]=TRUE;
        }
        if(rightCable){
            restraint[rightAnchorageJointIndex*2]=TRUE;
            restraint[rightAnchorageJointIndex*2+1]=TRUE;
        }
        int tt=0;
        for(t=0;t<MAX_FIXED_JOINT*2;t++){
            if(restraint[t]){
                f->fixedIndex[tt++]=t;
            }
        }
        f->fixedIndex[tt]=-1;
    }
    buf+=BUF_OFFSET_JOINT;
    for(t=0;t<jointSize;t++){
        Int x,y;
        char xs[4],ys[4];
        sscanf(buf,"%3[- 0-9]%3[- 0-9]",xs,ys);
        sscanf(xs,"%d",&x);
        sscanf(ys,"%d",&y);
        buf+=BUF_LEN_JOINT;
        f->positionHint.xy[t*2]=x;
        f->positionHint.xy[t*2+1]=y;
    }
    int bundleSize=0;
    {
        int index;
        int ttt;
        //valid conversion?
        Byte* memberi=&f->typeHint.member[0];
        Byte *bundle=&f->typeHint.bundle[0];
        for(t=0;t<MAX_BUNDLE;t++){
            bundle[t]=-1;
        }
        for(t=0;t<memberSize;t++){
            char j1s[3],j2s[3],i3s[3];
            Int j1,j2;
            int i1,i2,i3;
            sscanf(buf,"%2[ 0-9]%2[ 0-9]%1d%1d%2[ 0-9]",j1s,j2s,&i1,&i2,i3s);
            sscanf(j1s,"%d",&j1);
            sscanf(j2s,"%d",&j2);
            sscanf(i3s,"%d",&i3);
            buf+=BUF_LEN_MEMBER;
            f->memberLinks[t].j1=j1-1;
            f->memberLinks[t].j2=j2-1;
            index=GET_TYPE_INDEX(i1,i2,i3);
            for(ttt=0;ttt<bundleSize&&bundle[ttt]!=index;ttt++){}
            if(ttt==bundleSize){
                if(bundleSize==MAX_BUNDLE){
                    //exceed max bundle
                    printf("ERROR: max bundle!\n");
                    return 0;
                }
                
                bundle[bundleSize]=(Byte)index;
                printf("new bundle: %d %d %s\n",bundleSize,index,f->types[index].name);
                bundleSize++;
            }
            memberi[t]=ttt;
        }
        for(t=0;t<bundleSize;t++){
            int tt;
            for(tt=0;tt<MAX_TYPE;++tt){
                if(f->types[tt].index==bundle[t]){
                    bundle[t]=tt;
                    break;
                }
            }
        }
    }
    f->baseCost=0;
    f->typeHint.cost=10000000.;
    return f;
}

const BridgeInfo* rebaseBridge(const BridgeInfo* bridgeInfo,gpointer thc_ph){
    BridgeInfo* f=g_memdup(bridgeInfo,sizeof(BridgeInfo));
    f->buf=strdup(f->buf);
    gchar *buf2=f->buf+BUF_OFFSET_JOINT+f->fixedJointSize*BUF_LEN_JOINT;
    //move joint
    PositionHintB *position=(PositionHintB*)(thc_ph+TYPE_HINT_COST_SIZE(f->memberSize));
    int t1;
    for(t1=f->fixedJointSize;t1<f->totalJointSize;++t1){
        int t2=t1-f->fixedJointSize;
        f->positionHint.xy[t1*2  ]+=((((position->joints[t2]>>4)&15)^8)-8);
        f->positionHint.xy[t1*2+1]+=((((position->joints[t2]   )&15)^8)-8);
        sprintf(buf2,"%3d%3d",f->positionHint.xy[t1*2  ],f->positionHint.xy[t1*2+1]);
        buf2+=BUF_LEN_JOINT;
    }

    //change typeHint
    memcpy(&f->typeHint,thc_ph,TYPE_HINT_COST_SIZE(f->memberSize));
    
    char end=*(buf2+BUF_LEN_MEMBER*f->memberSize);
    for(t1=0;t1<f->memberSize;t1++){
        int i=f->types[f->typeHint.bundle[f->typeHint.member[t1]]].index;
        int i1,i2,i3;
        SET_TYPE_INDEX(i,i1,i2,i3);
        sprintf(buf2,"%2d%2d%1d%1d%2d",f->memberLinks[t1].j1+1,f->memberLinks[t1].j2+1,i1,i2,i3);
        buf2+=BUF_LEN_MEMBER;
    }
    *buf2=end;
    //TODO write report?
    return f;
}

void saveBridge(const char* path,const BridgeInfo *bridgeInfo){
    writeFile(path,bridgeInfo->buf,TRUE);
}
