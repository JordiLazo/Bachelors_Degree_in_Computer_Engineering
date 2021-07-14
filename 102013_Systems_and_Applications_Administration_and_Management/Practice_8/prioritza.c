#include <stdio.h>
#include <stdlib.h>
#include <sched.h>
#include <unistd.h>

int main(int argc, char** argv){
	
	if (argc != 2){
		printf("Sisplau, escriu un argument\n");
		exit(-1);
	}

	int pid = atoi(argv[1]);
	struct sched_param self, parent, parameter;

	self.sched_priority = 20;
	parameter.sched_priority = 10;
	parent.sched_priority = 30;

	sched_setscheduler(getpid(), SCHED_RR, &self);
	sched_setscheduler(pid, SCHED_RR, &parameter);
	sched_setscheduler(getppid(), SCHED_RR, &parent);
}