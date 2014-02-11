
#include "log.h"
#include <stdio.h>

const gchar* log_get_flag(GLogLevelFlags flag){
    static gchar buf[]="[RFECWMID]";
    //fatal,error,critical,warning,message,info,debug
    static const gchar cbuf[]={'R','F','E','C','W','M','I','D'};
    int t=0;
    for(t=0;t<8;t++){
        buf[t+1]=(flag&(1<<t))==0?' ':cbuf[t];
    }
    return buf;
}

void log_func_stdout(const gchar *log_domain,GLogLevelFlags log_level,const gchar *message,gpointer user_data){
    puts(message);
}
void log_func_stderr(const gchar *log_domain,GLogLevelFlags log_level,const gchar *message,gpointer user_data){
    fprintf(stderr,"%s",message);
}
void log_func_iochannel(const gchar *log_domain,GLogLevelFlags log_level,const gchar *message,gpointer user_data){
    g_io_channel_write_chars(user_data,log_get_flag(log_level),-1,NULL,NULL);
    g_io_channel_write_chars(user_data,message,-1,NULL,NULL);
}
