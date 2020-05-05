#!/usr/bin/env python3
import time
from itertools import islice
import linecache

def read_file_DFA():
    automat_file = open("deterministic_finite_automaton", "r")
    automat_state = []

    line_is = linecache.getline("deterministic_finite_automaton",2)
    inicial_state = line_is.strip('\n').split(' ')

    line_fs = linecache.getline("deterministic_finite_automaton",4)
    final_state = line_fs.strip('\n').split(' ')

    with open('deterministic_finite_automaton') as a:
        for line in islice(a,4,None):
            for third_line in a:
                automat_state.append(third_line.strip('\n').split(' '))
    print("Llegint arxiu...\n")
    for x in automat_file:
        print(x)
        time.sleep(1.5)

    print("Processant arxiu...\n")
    time.sleep(2)
    automat_file.close()

    return [inicial_state,final_state,automat_state]

def read_file_NDFA():
    automat_file = open("non_deterministic_finite_automaton", "r")
    automat_state = []

    line_is = linecache.getline("non_deterministic_finite_automaton",2)
    inicial_state = line_is.strip('\n').split(' ')

    line_fs = linecache.getline("non_deterministic_finite_automaton",4)
    final_state = line_fs.strip('\n').split(' ')

    with open('non_deterministic_finite_automaton') as a:
        for line in islice(a,4,None):
            for third_line in a:
                automat_state.append(third_line.strip('\n').split(' '))

    print("Llegint arxiu...\n")
    for x in automat_file:
        print(x)
        time.sleep(1.5)

    print("Processant arxiu...\n")
    time.sleep(2)
    automat_file.close()

    return [inicial_state,final_state,automat_state]

def read_word(automat,word):
    inicial_state = automat[0]
    final_state = automat[1]
    automat_state = automat[2]
    state_to_read = inicial_state
    state_next_read = []

    for i in word:
        for j in state_to_read:
            for n in automat_state:
                if j == n[0] and i == n[1]:
                    state_next_read.append(n[2])
        state_to_read = state_next_read
        state_next_read = []

    for z in state_to_read:
        for y in final_state:
            if z == y:
                return True

    return False


if __name__ == "__main__":

    selection = str(input("Introdueix el número '1' per llegir un AFD o el número '2' per llegir un AFND\n"))

    if selection == '1':
        automat = read_file_DFA()
        word_introduced = str(input("Introdueix una paraula a llegir:\n"))

        while (word_introduced != 'exit'):
            word_accepted = read_word(automat,word_introduced)
            if word_accepted:
                print("La paraula introduïda '"+ word_introduced + "' SI és ACCEPTADA per el autòmat.\n")
            else:
                print("La paraula introduïda '"+ word_introduced + "' NO és ACCEPTADA per el autòmat.\n")

            word_introduced = str(input("Introdueix una paraula a llegir o escriu 'exit' per finalitzar el programa\n"))

    if selection == '2':
        automat = read_file_NDFA()
        word_introduced = str(input("Introdueix una paraula a llegir:\n"))

        while (word_introduced != 'exit'):
            word_accepted = read_word(automat, word_introduced)
            if word_accepted:
                print("La paraula introduïda '" + word_introduced + "' SI és ACCEPTADA per el autòmat.\n")
            else:
                print("La paraula introduïda '" + word_introduced + "' NO és ACCEPTADA per el autòmat.\n")

            word_introduced = str(input("Introdueix una paraula a llegir o escriu 'exit' per finalitzar el programa\n"))