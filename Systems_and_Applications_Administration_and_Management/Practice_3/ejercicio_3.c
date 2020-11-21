#include <syslog.h>
//in terminal: cat /var/log/syslog
int main(){

	openlog("ex3", 0, LOG_LOCAL0);
	for (int i = 1; i <= 3; i++){
	    syslog(LOG_INFO, "Message nº%i", i);
	}
    closelog();
}
