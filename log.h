
#ifndef LOG_H
#define LOG_H

#include <glib.h>
#define FLAG_SIZE 8_
const gchar* log_get_flag(GLogLevelFlags flag);

void log_func_FILE(const gchar *log_domain,GLogLevelFlags log_level,const gchar *message,gpointer user_data);

void log_func_iochannel(const gchar *log_domain,GLogLevelFlags log_level,const gchar *message,gpointer user_data);

#endif

