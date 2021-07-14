/*
 * Copyright (c) 2018 Logic Optimization Group @ UdL
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "run_solver.h"
#include "sudoku.h"
#include "teacher.h"


void print_help_message(const char* program, bool full_help) {
    printf("Sudoku to SAT\n\n");
    printf("Usage: %s [-h|--help] <sudoku_file>\n\n", program);
    printf("    *   -h | --help: prints this message and exits.\n");
    printf("    * <sudoku_file>: file that defines a sudoku.\n");

    if (full_help) {
        printf("\n== <sudoku_file> format ==\n\n");
        printf("This file defines a square shaped sudoku.\n"
               "The first line consists of two numbers that describe the number"
               " of rows and\ncolumns in each region. Subsequently, the sudoku"
               " will have as many squared\nregions as the multiplication"
               " of these numbers (while preserving a square\nshape).\n");
        printf("The rest of the lines specify the numbers in each row of the"
               " sudoku, the\nspecial value 0 is used to indicate that a cell"
               " is empty.\n");
        printf("\nExample of a 2x2 sudoku:\n\n");
        printf("        2 2\n"
               "        1 2 0 0\n"
               "        0 0 1 0\n"
               "        2 0 0 3\n"
               "        0 3 0 1\n\n");
    }
}


int x(Sudoku* s, int i, int j, int k) {
    const int n = s->n_values;
    return i * n * n + j * n + k + 1;
}


void alo(FILE* f, int *vars, int size) {
    /* YOUR CODE HERE */
    for(int i=0;i<size;i++) {
    	fprintf(f,"%d ",vars[i]);
    }
    fprintf(f,"0\n");
}


void amo(FILE* f, int *vars, int size) {
    /* YOUR CODE HERE */
    for(int i=0;i<size-1;i++){
		for(int j=i+1;j<size;j++){
			fprintf(f,"-%d -%d 0\n",vars[i],vars[j]);
		}
	}
}


void eo(FILE* f, int *vars, int size){
    /* YOUR CODE HERE */
    alo(f,vars,size);
    amo(f,vars,size);
}


int main(int argc, char** argv)
{
    if (argc < 2) {
        print_help_message(argv[0], false);
        return EXIT_FAILURE;
    }

    /* look for help flag -h/--help */
    for (int i = 0; i < argc; ++i) {
        if (strcmp("-h", argv[1]) == 0 || strcmp("--help", argv[1]) == 0) {
            print_help_message(argv[0], true);
            return EXIT_SUCCESS;
        }
    }

    /* creating and loading the sudoku */
    Sudoku* sudoku = sudoku_new();

    int error_code = sudoku_parse_file(argv[1], sudoku);
    if (error_code == 0) {
        printf("Loaded sudoku\n");
        sudoku_print(stdout, sudoku);
    } else {
        printf("Error: %s\n", sudoku_translate_error_code(error_code));
        return EXIT_FAILURE;
    }

    /* create your formula here. */
    const int n = sudoku->n_values;
    const int n_fixed = sudoku->n_fixed_cells;
    int num_vars = n * n * n;                        /* ADJUST AS NECESSARY */
    int num_clauses = n * n * (1 + (n * (n-1) / 2)); /* ADJUST AS NECESSARY */

    FILE* f = fopen("instance.cnf","w");      /* file to save the instance */
    fprintf(f, "p cnf %d %d\n", num_vars, num_clauses); /* instance header */

    int* vars = (int*) malloc(n * sizeof(int));

    /* only one value per cell */
    fprintf(f, "c Cell constraints\n");
    for(int i = 0; i < n; i++) {
        for(int j = 0; j < n; j++) {
            for(int k = 0; k < n; k++){
                vars[k] = x(sudoku, i, j, k);
            }
            eo(f, vars, n);
        }
    }


    fprintf(f, "c Row constraints.\n");
    /* YOUR CODE HERE */
    for(int i = 0; i < n; i++) {
    	for(int k = 0; k < n; k++) {
    		for(int j = 0; j < n; j++){
    			vars[j] = x(sudoku, i, j, k);
    		}
    		eo(f, vars, n);
    	}
    }


    fprintf(f, "c Column constraints.\n");
    /* YOUR CODE HERE */
    for(int j = 0; j < n; j++) {
    	for(int k = 0; k < n; k++) {
    		for(int i = 0; i < n; i++){
    			vars[i] = x(sudoku, i, j, k);
            }
            eo(f, vars, n);
		}
    }


    fprintf(f, "c Region constraints.\n");
    /* YOUR CODE HERE */


    fprintf(f, "c Fixed number constraints.\n");
    /* YOUR CODE HERE */


    /* formula created: clean up & close the instance file */
    free(vars);
    fclose(f);

    /* solve the formula */
    int* model = (int*)malloc(sizeof(int) * num_vars);
    RunSolverCode rs_code = run_solver("./glucose -model", "instance.cnf",
                                         model);
    /*RunSolverCode rs_code = run_solver("./picosat", "instance.cnf",
                                       model);*/
    switch (rs_code) {
        case RUN_SOLVER_SAT:   /* formula is SAT, a solution has been found */
            printf("Formula is SAT. Model is:\n");
            for (int i = 0; i < num_vars; ++i) {
                printf("%d ", model[i]);
            }
            printf("\n");

            /* fill sudoku->cells using the model here */
            for(int i = 0; i < n; i++) {
                for(int j = 0; j < n; j++) {
                    for(int k = 0; k < n; k++) {
                        if (model[i * n * n + j * n + k] > 0) {
               	            sudoku->cells[i][j] = k+1;
                        }
                    }
                }
            }

            /* print the sudoku solution recovered from the model */
            sudoku_print(stdout, sudoku);
            break;
        case RUN_SOLVER_UNSAT:  /* formula is UNSAT, there is no solution */
            printf("Formula is UNSAT\n");
            break;
        case RUN_SOLVER_UNKNOWN:
            printf("Solver reported UNKNOWN\n");
            break;
        default:
            printf("something unexpected happened :(\n");
    }

    /* clean up and exit */
    free(model);
    sudoku_delete(sudoku);

    return EXIT_SUCCESS;
}
