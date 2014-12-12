// CS 61C Fall 2014 Project 3

// include SSE intrinsics
#if defined(_MSC_VER)
#include <intrin.h>
#elif defined(__GNUC__) && (defined(__x86_64__) || defined(__i386__))
#include <x86intrin.h>
#endif
// include OpenMP
#if !defined(_MSC_VER)
#include <pthread.h>
#endif
#include <omp.h>

#include "calcDepthOptimized.h"
#include "calcDepthNaive.h"
#include <math.h>
#include <stdbool.h>
#include "utils.h"

#define ABS(x) (((x) < 0) ? (-(x)) : (x))

#define MIN(a,b) ((a) < (b) ? a : b)

void calcDepthOptimized(float *depth, float *left, float *right, int imageWidth, int imageHeight, int featureWidth, int featureHeight, int maximumDisplacement)
{

	for(int i = 0; i < imageWidth*imageHeight; i ++) {
		depth[i] = 0;
	}

	int remaining = (2*featureWidth+1) % 4;
	int yDiff=imageHeight-featureHeight;
	int xDiff=imageWidth-featureWidth;
	#pragma omp parallel
	{
	    #pragma omp for schedule(dynamic)
		for (int x = featureWidth; x < xDiff; x++)
		{

			for (int y = featureHeight; y < yDiff; y++)
			{	


				int get_index=y * imageWidth + x;

				float minimumSquaredDifference = -1;
				int minimumDy = 0;
				int minimumDx = 0;

				int left_dy_bound=-MIN(maximumDisplacement,y-featureHeight);
				int left_dx_bound=-MIN(maximumDisplacement,x-featureWidth);
				int right_dy_bound=MIN(maximumDisplacement,yDiff-y-1);
				int right_dx_bound=MIN(maximumDisplacement,xDiff-x-1);

				for (int dx = left_dx_bound; dx <= right_dx_bound; dx++)
				{

					for (int dy = left_dy_bound; dy <= right_dy_bound; dy++)
					{



						float squaredDifference = 0;
						__m128 tempSqDiff = _mm_setzero_ps();


					    // Unroll by 8 boxX and boxY loop

						for(int boxX=-featureWidth;boxX<-featureWidth+(2*featureWidth+1)/8*8;boxX+=8)
						{
							for (int boxY = -featureHeight; boxY <=featureHeight; boxY++)
							{


								int leftX = x + boxX;
								int leftY = y + boxY;
								int rightX = x + dx + boxX;
								int rightY = y + dy + boxY;

								float* leftIndex=left+leftY*imageWidth+leftX;
								float* rightIndex=right+rightY*imageWidth+rightX;
								__m128 leftTemp = _mm_loadu_ps(leftIndex);
								__m128 rightTemp = _mm_loadu_ps(rightIndex);
								__m128 difference = _mm_sub_ps(leftTemp, rightTemp);

								tempSqDiff = _mm_add_ps(_mm_mul_ps(difference, difference), tempSqDiff);


								leftTemp = _mm_loadu_ps(leftIndex+4);
								rightTemp = _mm_loadu_ps(rightIndex+4);
								difference = _mm_sub_ps(leftTemp, rightTemp);

								tempSqDiff = _mm_add_ps(_mm_mul_ps(difference, difference), tempSqDiff);	

							}

						}


						float SUM[4] = {0, 0, 0, 0};
						__m128 tempSqDiff2 = _mm_hadd_ps(tempSqDiff, tempSqDiff);
						tempSqDiff2 = _mm_hadd_ps(tempSqDiff2, tempSqDiff2);
						_mm_storeu_ps(SUM, tempSqDiff2);
						float squaredDifference2 = SUM[0];

						if (minimumSquaredDifference != -1 && squaredDifference2 > minimumSquaredDifference){
							continue;
						}


					    // Unroll by 4 boxX and boxY loop
						for (int boxX=-featureWidth+(2*featureWidth+1)/8*8; boxX < -featureWidth+(2*featureWidth+1)/4*4; boxX+=4)
						{
							for (int boxY = -featureHeight; boxY <=featureHeight; boxY++)
							{


								int leftX = x + boxX;
								int leftY = y + boxY;
								int rightX = x + dx + boxX;
								int rightY = y + dy + boxY;

								__m128 leftTemp = _mm_loadu_ps(left + leftY*imageWidth + leftX);
								__m128 rightTemp = _mm_loadu_ps(right + rightY*imageWidth + rightX);
								__m128 difference = _mm_sub_ps(leftTemp, rightTemp);

								tempSqDiff = _mm_add_ps(_mm_mul_ps(difference, difference), tempSqDiff);

							}

						}


						// Tail case optimization

						int boxX = -featureWidth+(2*featureWidth+1)/4*4;
						if (remaining == 1) {

							for (int boxY = -featureHeight; boxY <=featureHeight; boxY++)
							{

								int leftX = x + boxX;
								int leftY = y + boxY;
								int rightX = x + dx + boxX;
								int rightY = y + dy + boxY;

								float difference = left[leftY * imageWidth + leftX] - right[rightY * imageWidth + rightX];

								squaredDifference += difference * difference;
							}
						}
						else {
							__m128 tempSqDiff3 = _mm_setzero_ps();

							for (int boxY = -featureHeight; boxY <=featureHeight; boxY++)
							{

								int leftX = x + boxX;
								int leftY = y + boxY;
								int rightX = x + dx + boxX;
								int rightY = y + dy + boxY;

								__m128 leftTemp = _mm_loadu_ps(left + leftY*imageWidth + leftX);
								__m128 rightTemp = _mm_loadu_ps(right + rightY*imageWidth + rightX);
								__m128 difference = _mm_sub_ps(leftTemp, rightTemp);

								tempSqDiff3 = _mm_add_ps(_mm_mul_ps(difference, difference), tempSqDiff3);
							}
							_mm_storeu_ps(SUM, tempSqDiff3);


							squaredDifference += SUM[0] + SUM[1] + SUM[2];
						}

						tempSqDiff = _mm_hadd_ps(tempSqDiff, tempSqDiff);
						tempSqDiff = _mm_hadd_ps(tempSqDiff, tempSqDiff);
						_mm_storeu_ps(SUM, tempSqDiff);

						squaredDifference += SUM[0];

						if ((minimumSquaredDifference == -1) || ((minimumSquaredDifference == squaredDifference) && (displacementNaive(dx, dy) < displacementNaive(minimumDx, minimumDy))) || (minimumSquaredDifference > squaredDifference))
						{
							minimumSquaredDifference = squaredDifference;
							minimumDx = dx;
							minimumDy = dy;
						}

					}

				}



				if (minimumSquaredDifference != -1)
				{
					depth[get_index] = displacementNaive(minimumDx, minimumDy);
				}
				else
				{
					depth[get_index] = 0;
				}
			}
		}
	}

}
