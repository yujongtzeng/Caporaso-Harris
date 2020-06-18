# Caporaso-Harris
This project implements the recursive formulas of Caporaso-Harris and Vakil to compute the number of nodal curves satisfying given tangency conditions with a line on the projective plane or Hirzebruch surfaces. 

The recursive formulae can be found here:
* [Caporaso-Harris](https://arxiv.org/pdf/alg-geom/9608025.pdf) Theorem 1.1
* [Vakil](https://link.springer.com/article/10.1007/s002291020053) Theorem 6.12

### Features
We improved the naive recursive algorithm by:
* dynamic approach algorithms
* letting users to choose the range of degrees and nodes
* testing pre-generated partitions alpha' and beta' for the second term instead of generating valid alpha' and beta' every time
* greatly improving the speed by limiting the range of computations in the second term in recursive formulae

### Installing
No installation is needed.

### How to use
First change directory to src/

For computation on the projective plane,
```
$ javac CH.java
$ java CH
```
For computation on Hirzebruch surfaces F_n for any n,
```
$ javac HirTable.java
$ java HirTable
```

The program will show you instructions to enter inputs and the location for output. It will print the number of nodal curves as well as their first terms for generating functions in the output files. 


### API Reference

The documentation is under [doc](/doc) directory.

### Tests

Run test.java under src directory. It compares the output with pre-generated files. Those test files contains but not limited all numbers on Caporaso-Harris and Vakil's paper.

### Technologies
Java 8

### Versioning

Version 4. 

### Authors

**Yu-jong Tzeng** 

### License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

