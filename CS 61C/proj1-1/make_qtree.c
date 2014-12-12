/*
 * PROJ1-1: YOUR TASK B CODE HERE
 *
 * Feel free to define additional helper functions.
 */

#include <stdlib.h>
#include <stdio.h>
#include "quadtree.h"
#include "make_qtree.h"
#include "utils.h"

#define ABS(x) (((x) < 0) ? (-(x)) : (x))

int homogenous(unsigned char *depth_map, int map_width, int x, int y, int section_width) {

    /* YOUR CODE HERE */
    
    int gray_value = (int)malloc(sizeof(int));
    if (!(gray_value)) {
        allocation_failed();
    }
    
    // base case
    if (section_width <= 1) {
        gray_value = (int) depth_map[x + y * map_width];
        return gray_value;
    }
    
    gray_value = homogenous(depth_map, map_width, x, y, section_width / 2); //NW

    if (gray_value == homogenous(depth_map, map_width, x + (section_width / 2), y, section_width / 2) //NE
        && gray_value == homogenous(depth_map, map_width, x, y + (section_width / 2), section_width /2) //SW
        && gray_value == homogenous(depth_map, map_width, x+(section_width / 2), y+(section_width / 2), section_width /2))  {//SE {
        return gray_value;
    }
    return 256;
    

}

qNode *makeNode(unsigned char *depth_map, int map_width, int size, int x, int y, int gray_value) {
    qNode *newNode = (qNode*)malloc(sizeof(qNode));
  
    if (newNode == NULL) {
        allocation_failed();
    }
    if (gray_value == 256) {
        newNode->leaf = 0;
        newNode->child_NW = makeNode(depth_map, map_width, size / 2, x, y, homogenous(depth_map, map_width, x, y, size / 2));
        newNode->child_NE = makeNode(depth_map, map_width, size / 2, x+(size/2), y, homogenous(depth_map, map_width, x+(size/2), y, size / 2));
        newNode->child_SE = makeNode(depth_map, map_width, size / 2, x+(size/2), y+(size/2), homogenous(depth_map, map_width , x+(size/2), y+(size/2), size / 2));
        newNode->child_SW = makeNode(depth_map, map_width, size / 2, x, y+(size/2), homogenous(depth_map, map_width, x, y+(size/2), size / 2));
    }
    else {
        newNode->leaf = 1;
    }
    newNode->size = size;
    newNode->x = x;
    newNode->y = y;
    newNode->gray_value = gray_value;

    return newNode;
}


qNode *depth_to_quad(unsigned char *depth_map, int map_width) {

    /* YOUR CODE HERE */
    qNode *root;
    int gray_value = homogenous(depth_map, map_width, 0, 0, map_width);
    root = makeNode(depth_map, map_width, map_width, 0, 0, gray_value);

    return root;

}

void free_qtree(qNode *qtree_node) {
    if(qtree_node) {
        if(!qtree_node->leaf){
            free_qtree(qtree_node->child_NW);
            free_qtree(qtree_node->child_NE);
            free_qtree(qtree_node->child_SE);
            free_qtree(qtree_node->child_SW);
        }
        free(qtree_node);
    }
}
