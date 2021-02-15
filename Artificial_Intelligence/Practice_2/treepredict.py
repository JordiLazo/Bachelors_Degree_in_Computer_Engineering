#! /usr/bin/env python2
# -*- coding: utf-8 -*-

class Queue:
    """A container with a first-in-first-out (FIFO) queuing policy."""

    def __init__(self):
        self.list = []

    def push(self, item):
        """Enqueue the 'item' into the queue"""
        self.list.insert(0, item)

    def pop(self):
        """
          Dequeue the earliest enqueued item still in the queue. This
          operation removes the item from the queue.
        """
        return self.list.pop()

    def is_empty(self):
        """Returns true if the queue is empty"""
        return len(self.list) == 0

    def size(self):
        """Returns the queue's size"""
        return len(self.list)


# --------------------- t1  ---------------------
# Download the data file descision_tree_example.txt from the virtual campus at folder/lab/learning.
# --------------------- t2  ---------------------
# Create a file named treepredict.py.
# --------------------- t3  ---------------------
# Define a function to load the data into a bidimensional list named data.
def read_file(file_path, data_sep=",", ignore_first_line=False):
    """ read files and converts into array"""
    prototypes = []
    # Open file
    with open(file_path, "r") as file_handler:
        # Strip lines
        strip_reader = (line.strip() for line in file_handler)

        # Filter empty lines
        filtered_reader = (line for line in strip_reader if line)

        # Skip first line if needed
        if ignore_first_line:
            next(filtered_reader)

        # Split lines, parse token and append to prototypes
        for line in filtered_reader:
            prototypes.append(
                [filter_token(token) for token in line.split(data_sep)]
            )

    return prototypes


def filter_token(token):
    """converts token into correct instance"""
    try:
        return int(token)
    except ValueError:
        try:
            return float(token)
        except ValueError:
            return token


# --------------------- t4  ---------------------
# Define a function unique_counts that counts the number of prototypes of a given class in a partition part.
# Create counts of possible results
# (the last column of each row is the result).
def unique_counts(part):
    """counts the values from the last column of dataset or part """
    # import collections
    # return dict(collections.Counter(row[-1] for row in part)
    results = {}
    for row in part:
        results[row[-1]] = results.get(row[-1], 0) + 1
    #    if row[-1] not in results.keys():
    #        results[row[-1]] = 1
    #    else:
    #        results[row[-1]] += 1

    return results


# --------------------- t5  ---------------------
# Define a function that computes the Gini index of a node.
def gini_impurity(part):
    """ calculates the gini index"""
    total = float(len(part))
    results = unique_counts(part)

    return 1 - sum((count / total) ** 2 for count in results.values())


# --------------------- t6  ---------------------
# Define a function that computes the entropy of a node.
def entropy(part):
    """ calculates the entropy index"""
    from math import log
    log2 = lambda x: log(x) / log(2)
    results = unique_counts(part)
    # Now calculate the entropy
    total = float(len(part))
    return -sum(
        (count / total) * log2(count / total) for count in results.values()
    )


# --------------------- t7  ---------------------
#  Define a function that partitions a previous partition, taking
# into account the values of a given attribute (column).
def divideset(part, column, value):
    """ splits dataset into two partitions"""
    if isinstance(value, int) or isinstance(value, float):
        split_function = lambda row: row[column] >= value
    else:
        split_function = lambda row: row[column] == value
    # Split "part accordinf "split_function"
    set1 = [row for row in part if split_function(row)]
    set2 = [row for row in part if not split_function(row)]
    return set1, set2


# --------------------- t8  ---------------------
# Define a new class decisionnode, which represents a node in the tree.
class Desicionnode:
    """ class node of decision tree"""

    def __init__(self, col=-1, value=None, results=None, tb=None, fb=None):
        self.col = col
        self.value = value
        self.results = results
        self.tb = tb
        self.fb = fb


# --------------------- t9  ---------------------
# Construcción del árbol de forma recursiva.
def buildtree(dataset, score_func=gini_impurity, beta=0):
    """builds desicion tree recursively"""
    if len(dataset) == 0:
        return Desicionnode()
    impurity = score_func(dataset)

    # best split criteria
    best_impurity_decrease, criteria, sets = split_dataset(dataset, impurity, score_func)

    if best_impurity_decrease > beta:
        return Desicionnode(col=criteria[0], value=criteria[1], tb=buildtree(sets[0]), fb=buildtree(sets[1]))
    else:
        return Desicionnode(results=unique_counts(dataset))


def split_dataset(dataset, impurity, score_func):
    """splits data set in two sets"""
    best_decrease_impurity = 0
    criteria = None
    sets = None
    for atribute_idx in range(len(dataset[0]) - 1):
        atribute_values = get_column_values(atribute_idx, dataset)
        for value in atribute_values:
            (set_t, set_f) = divideset(dataset, atribute_idx, value)
            impurity_t = score_func(set_t)
            impurity_f = score_func(set_f)
            impurity_decrease = impurity - ((float(len(set_t)) / len(dataset)) * impurity_t) - (
                    (float(len(set_f)) / len(dataset)) * impurity_f)

            if len(set_t) > 0 and len(set_f) > 0 and impurity_decrease > best_decrease_impurity:
                best_decrease_impurity = impurity_decrease
                criteria = (atribute_idx, value)
                sets = (set_t, set_f)
    return best_decrease_impurity, criteria, sets


def get_column_values(atribute_idx, dataset):
    """returns values of given column of dataset"""
    atribute_values = []
    for row in dataset:
        if row not in atribute_values:
            atribute_values.append(row[atribute_idx])
    return atribute_values


# --------------------- t10 ---------------------
# Construcción del árbol de forma iterativa.
def buildtree_iterative(dataset, score_func=gini_impurity, beta=0):
    """builds desicion tree iteratively"""
    if len(dataset) == 0:
        return Desicionnode()

    root = Desicionnode()

    tree_queue = Queue()
    queue = Queue()

    # True if is a Leaf
    # False if is a branch or root Node
    tree_queue.push(root)
    queue.push((dataset, False))

    while not queue.is_empty():
        part, leaf = queue.pop()
        node = tree_queue.pop()
        if not leaf:
            # Split dataset by impurity function and creates the branches
            best_impurity_decrease, criteria, sets = split_dataset(part, score_func(part), score_func)
            if best_impurity_decrease > beta:
                node.col = criteria[0]
                node.value = criteria[1]

                node.tb = Desicionnode()
                node.fb = Desicionnode()

                tree_queue.push(node.tb)
                tree_queue.push(node.fb)

                queue.push((sets[0], False))
                queue.push((sets[1], False))
            else:
                # It is a branch that will be treated as a leaf by the beta parameter
                # or just it is a leaf and sets it True
                queue.push((unique_counts(part), True))
                tree_queue.push(node)
        else:
            node.results = part
    return root


# --------------------- t11 ---------------------
# Include the following function printtree:
def printtree(tree, indent=''):
    """prints fancy tree """
    # Is this a leaf node?
    if tree.results is not None:
        print(indent + str(tree.results))
    else:
        # Print the criteria
        # Little modification to make clear split condition
        if isinstance(tree.value, int) or isinstance(tree.value, float):
            criteria_symbol = " >= " + str(tree.value)
        else:
            criteria_symbol = " == " + str(tree.value)
        print(indent + str(tree.col) + ':' + str(criteria_symbol) + '? ')
        # Print the branches
        print(indent + 'T->')
        printtree(tree.tb, indent + '  ')
        print(indent + 'F->')
        printtree(tree.fb, indent + '  ')


# --------------------- t12 ---------------------
# Build a function classify that allows to classify new objects.
# It must return the dictionary that represents the partition ofthe leave node where the object is classified.
def classify(object, tree):
    """allows to classify new objects"""
    split_function = None
    if isinstance(tree.value, (int, float)):
        split_function = lambda x: x[tree.col] >= tree.value
    else:
        split_function = lambda x: x[tree.col] == tree.value

    if tree.results is not None:
        return tree.results

    if split_function(object):
        return classify(object, tree.tb)

    return classify(object, tree.fb)


if __name__ == '__main__':
    prototypes = read_file("decision_tree_example.txt", data_sep=",", ignore_first_line=True)
    count = unique_counts(prototypes)
    giniIndex = gini_impurity(prototypes)
    entropyValue = entropy(prototypes)
    print("\nGini Index: " + str(giniIndex) + " Entropy: " + str(entropyValue))
    print(" --------------------- Iterative  --------------------- ")
    desisicion_tree = buildtree_iterative(prototypes)
    printtree(desisicion_tree)

    print(" --------------------- Recursion --------------------- ")
    tree = buildtree(prototypes)
    printtree(tree)
