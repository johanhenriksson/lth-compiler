#!/usr/bin/python
#
# gen-tree.py
# johan henriksson (dat11jhe)
#
# generates a simplic test case that sets up a binary tree with N 
# nodes, counts them and assert that the count is correct.
#
# usage: python gen-tree.py [number of nodes] > output_file.simplic

import sys

class Node:
    parent = False
    left = False
    right = False

    def __init__(self, val):
        self.value = val

    def add(self, val, depth = 0):
        if not self.left:
            self.left = Node(val)
            self.left.parent = self
            return True
        if not self.right:
            self.right = Node(val)
            self.right.parent = self
            return True
        return depth > 0 and \
            self.left.add(val, depth - 1) or \
            self.right.add(val, depth - 1)

    def write(self):
        name = "n%d" % self.value
        print "\tnode %s = new_node(%d);" % (name, self.value)

        if self.parent:
            print "\t%s.parent = n%d;" % (name, self.parent.value)

        if self.left:
            self.left.write()
            print "\t%s.left = n%d;" % (name, self.left.value)
            print "\t%s.has_left = true;" % name

        if self.right:
            self.right.write()
            print "\t%s.right = n%d;" % (name, self.right.value)
            print "\t%s.has_right = true;" % name
 

if len(sys.argv) <= 1:
    print "// usage: python gen-tree.py [number of nodes]"
    exit()

count = int(sys.argv[1])
root = Node(1)

# generate nodes
i = 2
depth = 0
while i <= count:
    success = root.add(i, depth)
    if not success:
        depth += 1
        continue
    i += 1

# output simplic code
print "struct node {\n\tint value;\n\tnode parent;\n\tnode left;\n\tnode right;\n\tbool has_left;\n\tbool has_right;\n}\nnode new_node(int val) {\n\tnode n;\n\tn.value = val;\n\tn.has_left = false;\n\tn.has_right = false;\n\treturn n;\n}\nint nodes(node n) {\n\tint i = 1;\n\tif (n.has_left) {\n\t\ti = i + nodes(n.left);\n\t}\n\tif (n.has_right) {\n\t\ti = i + nodes(n.right);\n\t}\n\treturn i;\n}\n"
print "int main() {"
root.write()
print "\n\tassert(nodes(n1) == %d);" % count
print "\treturn 0;"
print "}"
