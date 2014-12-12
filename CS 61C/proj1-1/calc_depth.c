/*
 * PROJ1-1: YOUR TASK A CODE HERE
 *
 * Feel free to define additional helper functions.
 */

#include "calc_depth.h"
#include "utils.h"
#include <math.h>
#include <limits.h>
#include <stdio.h>

/* Implements the normalized displacement function */
 unsigned char normalized_displacement(int dx, int dy,
    int maximum_displacement) {

    double squared_displacement = dx * dx + dy * dy;
    double normalized_displacement = round(255 * sqrt(squared_displacement) / sqrt(2 * maximum_displacement * maximum_displacement));
    return (unsigned char) normalized_displacement;

}

unsigned convert_index(unsigned x, unsigned y, int image_width){
    int index=x+image_width*y;
    //printf("%s\n", "stuck10");
    return index;
}
unsigned squared_euclidean_distance(unsigned x, unsigned y, unsigned target_x, unsigned target_y, int feature_width, int feature_height, unsigned char* left, unsigned char* right, int image_width, int image_height){
    //printf("%s\n", "stuck6");
    int sum=0;
    if(feature_width==0&&feature_height==0){
        //printf("%s\n", "stuck7");

        sum=sum+pow(left[convert_index(target_x,target_y,image_width)]-right[convert_index(x,y,image_width)], 2);

    }
    if(feature_width!=0&&feature_height!=0){
       //printf("%s\n", "stuck8");
       // for(int ty=target_y-feature_height; ty<=target_y+feature_height;ty++){
       //  for(int tx=target_x-feature_width; tx<=target_x+feature_width;tx++){
        for(int fy=y-feature_height;fy<=y+feature_height;fy++){
            for(int fx=x-feature_width;fx<=x+feature_width;fx++){
                int x_distance=fx-x;
                int y_distance=fy-y;
                            //printf("%s\n", "stuck9");
                if(left[convert_index(target_x+x_distance,target_y+y_distance,image_width)]>right[convert_index(fx,fy,image_width)]){
                    sum+=pow(left[convert_index(target_x+x_distance,target_y+y_distance,image_width)]-right[convert_index(fx,fy,image_width)], 2);
                }else{
                    sum+=pow(right[convert_index(fx,fy,image_width)]-left[convert_index(target_x+x_distance,target_y+y_distance,image_width)], 2);
                }
                    //sum+=pow(left[convert_index(target_x+x_distance,target_y+y_distance,image_width)]-right[convert_index(fx,fy,image_width)], 2);
                //printf("%s\n", "SED");
            }
        }
    //     }
    // }

    }
    else if(feature_width!=feature_height){
        printf("%s\n", "invalid feature");
    }
    //printf("%s\n", "single feature SED done");
    return sum;
}
unsigned is_out_of_bounds(unsigned x, unsigned y, unsigned width, unsigned height, unsigned image_width, unsigned image_height){

    if((x+width>=image_width)||(x<width)||(y+height>=image_height)||(y<height)){
        return 1;
    }

    return 0;
}
void calc_depth(unsigned char *depth_map, unsigned char *left,
    unsigned char *right, int image_width, int image_height,
    int feature_width, int feature_height, int maximum_displacement) {

    /* YOUR CODE HERE */
    //printf("%s\n", "stuck");
    //printf("%d\n", maximum_displacement);


    for(int y=0;y<image_height;y++){
        for(int x=0;x<image_width;x++){



            //printf("(%x %x)\n", x, y);

            /* for each pixel in the left image, pixel C */
            /* draw a box centered at C */
            // for(int fx=x-feature_width;fx<x+feature_width;fx++){
            //     for(int fy=y-feature_height;fy<y+feature_height;fy++){

            //     }
            // }
            if (is_out_of_bounds(x,y,feature_width, feature_height, image_width, image_height)){
            /* check if the feature centered with this pixel is out of the image bound */
                //printf("%s\n", "out of bound");

                depth_map[convert_index(x,y,image_width)] = 0;
                continue;
            }

            if(maximum_displacement==0){
                depth_map[convert_index(x,y,image_width)]=0;
                //printf("%s\n", "max is 0");
                continue;
            }
            if(!is_out_of_bounds(x,y,feature_width, feature_height, image_width, image_height)){

                int x_pos=0;
                int y_pos=0;
                int smallest_sofar=-1;



                for(int j=y-maximum_displacement;j<=y+maximum_displacement;j++){
                    for(int i=x-maximum_displacement;i<=x+maximum_displacement;i++){
                        // if(is_out_of_bounds(i,j,maximum_displacement, maximum_displacement, image_width, image_height)){
                        //     depth_map[convert_index(x,y,image_width)] = 0;
                        // }
                        int temp=0;

                        // printf("%s\n", "center");

                    /* for each center pixel of the feature in the search box */

                        if(!(i<0||j<0||i>=image_width||j>=image_height)){
                                //printf("%x %x\n", i, j);
                                // int target_x=i;
                                // int target_y=j;
                            if(!is_out_of_bounds(i,j,feature_width, feature_height, image_width, image_height)){
                                temp=squared_euclidean_distance(i, j, x, y, feature_width, feature_height, left,  right, image_width, image_height);
                            // if(i==14&&j==4&&x==14&&y==4){
                            //     printf("%s\n", "*********************************************");

                            //     printf("%d\n", temp);
                            //     printf("%s\n", "*********************************************");
                            // }
                            //printf("%d\n", temp);

                                if(smallest_sofar==-1||temp<smallest_sofar){
                                    //printf("%s\n", "stuck4");

                                    smallest_sofar=temp;
                                    x_pos=i;
                                    y_pos=j;

                                }
                                if(smallest_sofar==temp){
                                    //printf("%s\n", "true");

                                    int old_norm=normalized_displacement(x_pos-x, y_pos-y, maximum_displacement);
                                    int new_norm=normalized_displacement(i-x,j-y,maximum_displacement);
                                     //printf("%d\n", old_norm);
                                     //printf("%d\n", new_norm);
                                    if(new_norm<=old_norm){
                                        //printf("%s\n", "stuck5");

                                        x_pos=i;
                                        y_pos=j;
                                        //printf("%x %x\n", x_pos, y_pos);

                                    }
                                }
                            }
                            
                                //printf("%x %x\n", x_pos, y_pos);
                            //depth_map[convert_index(x,y, image_width)]=normalized_displacement(x_pos-x,y_pos-y,maximum_displacement);
                        }
                        depth_map[convert_index(x,y, image_width)]=normalized_displacement(x_pos-x,y_pos-y,maximum_displacement);
                    }
                }
                     //depth_map[convert_index(x,y, image_width)]=normalized_displacement(x_pos,y_pos,maximum_displacement);
                //}
            }
        }
    }
    //printf("%d\n", feature_pixels);

}



