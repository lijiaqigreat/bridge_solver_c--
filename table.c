
#include "table.h"
CostTable *table_init(CostTable *f,int size1,int size2,float limit){
    if(f==NULL){
        f=g_new(CostTable,1);
    }
    f->size1=size1;
    f->size2=size2;
    f->size2_=0;
    f->limit=limit;
    f->data=g_malloc((size1+sizeof(Dollar))*size2);
    int t;
    for(t=0;t<size2;t++){
        (*(Dollar*)(f->data+t*(f->size1+sizeof(Dollar))+f->size1))=EMPTY_VALUE;
    }
    return f;
}

int table_hash(gconstpointer element,int size1,int size2){
    gsize f=0;
    int t=0;
    for(t=0;t<size1;t++){
        f=f*65599+GET_BYTE(element+t);
    }
    return f%size2;
}

Dollar table_peek(const CostTable *table,gconstpointer element){
    int size1=table->size1;
    int i=table_hash(element,size1,table->size2);
    gpointer data=table->data+(size1+sizeof(Dollar))*i;
    while(GET_VALUE(data)!=EMPTY_VALUE){
        if(memcmp(element,data,size1)==0){
            return GET_VALUE(data);
        }else{
            data+=size1+sizeof(Dollar);
            i++;
            if(i==table->size2){
                data=table->data;
                i=0;
            }
        }
    }
    return EMPTY_VALUE;
}

Dollar _table_insert(CostTable *table,gconstpointer element,Dollar cost);

Dollar table_insert(CostTable *table,gconstpointer element,Dollar cost){
    //expand?
    if(table->size2*table->limit<table->size2_+1){
        int size1=table->size1;
        int size2=table->size2;
        //old data
        gpointer data1=table->data;
        gpointer data1_=data1;
        //new data
        table->data=g_malloc((size1+sizeof(Dollar))*size2*2);
        table->size2*=2;
        table->size2_=0;

        //initialize data2
        int t;
        for(t=0;t<size2*2;t++){
            GET_VALUE(table->data+t*(size1+sizeof(Dollar)))=EMPTY_VALUE;
        }

        //insert all key-value pairs
        for(t=0;t<size2;t++){
            if(GET_VALUE(data1)!=EMPTY_VALUE){
                _table_insert(table,data1,GET_VALUE(data1));
            }
            data1+=size1+sizeof(Dollar);
        }
        free(data1_);
        data1_=table->data;
    }
    //get hash
    return _table_insert(table,element,cost);
}

Dollar _table_insert(CostTable *table,gconstpointer element,Dollar cost){
    int size1=table->size1;
    int size2=table->size2;
    int i=table_hash(element,size1,size2);
    gpointer data=table->data+(size1+sizeof(Dollar))*i;

    //probe
    while(*(Dollar*)(data+size1)!=EMPTY_VALUE){
        if(memcmp(element,data,size1)==0){
            //TODO key exist
            return *(Dollar*)(data+size1);
        }else{
            data+=size1+sizeof(Dollar);
            i++;
            if(i==size2){
                data=table->data;
                i=0;
            }
        }
    }
    //copy key, value
    memcpy(data,element,size1);
    memcpy(data+size1,&cost,sizeof(Dollar));
    //increase size
    table->size2_++;
    return EMPTY_VALUE;
}

void table_free(CostTable *table){
    free(table->data);
    free(table);
}

void table_print(CostTable *table){
    printf("--- table ---\n");
    printf("%4d|%4d|%4d|%1.2f\n",table->size1,table->size2,table->size2_,table->limit);
    //print all key-value pairs
    int t;
    gpointer data=table->data;
    int size1=table->size1;
    for(t=0;t<table->size2;t++){
        if(GET_VALUE(data)!=EMPTY_VALUE){
            printf("%s:%5.2f\n",print_bytes(data,table->size1),GET_VALUE(data));
        }
        data+=size1+sizeof(Dollar);
    }
    printf("--- end table ---\n");
}
