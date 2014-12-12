.text

# Decodes a quadtree to the original matrix
#
# Arguments:
#     quadtree (qNode*)
#     matrix (void*)
#     matrix_width (int)
#
# Recall that quadtree representation uses the following format:
#     struct qNode {
#         int leaf;
#         int size;
#         int x;
#         int y;
#         int gray_value;
#         qNode *child_NW, *child_NE, *child_SE, *child_SW;
#     }



quad2matrix:
        # load a3
        # li $a3 0
        # addu $a3 $a3 $a2
        j HELPER
        
HELPER:
        # YOUR CODE HERE #
        beqz $a0 RETURN
        lw $s0 0($a0) # leaf 
    
        beq $s0 1 FORLOOP # if(leaf)
        j RECURSE
        
FORLOOP:
        lw $s1 8($a0) # quadtree->x
        lw $s2 12($a0) # quadtree->y
        lw $t8 4($a0) # size
        
L1:
        li $t1 0
        li $t3 0
        move $t1 $s2 # y=quadtree->y       
        addu $t3 $s2 $t8 # quadtree->y+size
        
LOOP1:  
        slt $t4 $t1 $t3 # y<(quadtree->y+size)
        bne $t4 1 L6 # go to end
        j L2

        
L2:     li $t0 0
        li $t2 0
        move $t0 $s1 # x=quadtree->x
        addu $t2 $s1 $t8 # quadtree->x+size
        
LOOP2:
        slt $t5 $t0 $t2 # x<(quadtree->x+size)
        bne $t5 1 L5 # go to L5
        
        j L3
        
L3:
        mul $t6, $t1, $a2 # set temp to matrix_width*y
        addu $t6, $t6, $t0 # set temp to x+matrix_width*y
        #sll $t6, $t6, 0
        
        addu $t7, $t6,$a1
        # lbu $t7, 0($t6)
        lw $s3 16($a0) # gray_value
        # $t6 $s3
        
        sb $s3, 0($t7)
        

        #addu $t7, $t7, $s3 # matrix[x+matrix_width*y]=quadtree->gray_value

L4:
        addu $t0 $t0 1 # x++
        j LOOP2
        
L5:     move $t0 $s1
        addu $t1 $t1 1 # y++
        j LOOP1
L6:
        move $t1 $s2
        j RETURN                

        
                        
RECURSE:


        
        sub $sp, $sp, 32
        sw $ra 0($sp)
        sw $a0 4($sp)
        sw $a1 8($sp)
        sw $a2 12($sp)

        sw $s4 16($sp)
        sw $s5 20($sp)
        sw $s6 24($sp)
        sw $s7 28($sp)

        
        
        lw $s4 20($a0) # child_NW
        lw $s5 24($a0) # child_NE
        lw $s6 28($a0) # child_SE
        lw $s7 32($a0) # child_SW

        
C1:
        move $a0 $s4  
           
        jal HELPER   
        
                
C2:
        move $a0 $s5
                      
        jal HELPER
       
C3:
        move $a0 $s6
                  
        jal HELPER     
C4:

        move $a0 $s7
             
        jal HELPER
        
        
        lw $ra 0($sp) 

        lw $a0 4($sp)
        lw $a1 8($sp)
        lw $a2 12($sp)

        lw $s4 16($sp)
        lw $s5 20($sp)
        lw $s6 24($sp)
        lw $s7 28($sp)

        
        addiu $sp $sp 32
             
       
RETURN:
        jr $ra
