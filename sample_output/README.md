# How to use

The files in this directory is the output of CH.java and HirTable.java. 

In the CH directory, the file O(i)_r=j.txt contains j-nodal curves of degree i as studied by Caporaso-Harris on P^2. The genCH directory contains their generating series.

In the Hir directory, the file ah+bf_g=h.txt in directory Fn contains the number of nodal curves in curve class ah+bf of geometric genus h on the Hirzebruch surfaces F_n. The genHir directory contains their generating series.

### Instruction
When the degree is larger than 25 on projective plane, the file size is too large so we use the split tool in mac to split the file into several parts by the command
>> split -b 20m "O(30)_r=1.txt" "O(30)_r=1".

or loop 

>> for i in {0..4}; do split -b 20m "O(30)_r=${i}.txt" "O(30)_r=${i}"; done

For example, the O(30)_r=4.txt file is splitted into five files: <br>
O(30)_r=4aa <br>
O(30)_r=4ab <br>
O(30)_r=4ac <br>
O(30)_r=4ad <br>
O(30)_r=4ae

Each of the split file still can be read directly from text editor, or can be merged again by
>> cat "O(30)_r=0"?? > "O(30)_r=0.txt"

The output files for HirTable.java is split similarly.
