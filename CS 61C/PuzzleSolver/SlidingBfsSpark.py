from pyspark import SparkContext
import Sliding, argparse


def bfs_flat_map(value):
    """ YOUR CODE HERE """
    # childrenLst=[]    
    # childrenLst.append(value)
    # for child in Sliding.children(WIDTH,HEIGHT,Sliding.hash_to_board(WIDTH, HEIGHT, value[0])):
    #     pair=[]
    #     pair.append(Sliding.board_to_hash(WIDTH, HEIGHT, child))
    #     pair.append(level)     
    #     childrenLst.append(tuple(pair))
       
    # return childrenLst

    childrenLst = []
    childrenLst.append((value[0], value[1]))
    if(value[1] == level - 1):
        for child in Sliding.children(WIDTH, HEIGHT, Sliding.hash_to_board(WIDTH,HEIGHT, value[0])):
            childrenLst.append((Sliding.board_to_hash(WIDTH,HEIGHT,child), value[1]+1))
    return childrenLst 
def bfs_map(value):
    """ YOUR CODE HERE """
    return (value,level)

def bfs_reduce(value1, value2):
    """ YOUR CODE HERE """
    return min(value1,value2)

def solve_puzzle(master, output, height, width, slaves):
    global HEIGHT, WIDTH, level
    HEIGHT=height
    WIDTH=width
    level = 0

    sc = SparkContext(master, "python")

    """ YOUR CODE HERE """
    """ YOUR MAP REDUCE PROCESSING CODE HERE """
    solution=Sliding.solution(WIDTH, HEIGHT)
    sol = Sliding.board_to_hash(WIDTH, HEIGHT, solution)
    data = sc.parallelize([(sol,level),])
    counter = 0
    curLen = 1 
    while(counter < curLen):
        level += 1
        data = data.flatMap(bfs_flat_map)
        

        if (level% 12 == 0):
            data = data.partitionBy(PARTITION_COUNT)
        data = data.reduceByKey(bfs_reduce)
        if (level% 6 == 0):
            counter = curLen
            curLen = data.count()
        
        
    """ YOUR OUTPUT CODE HERE """
    data.coalesce(slaves).saveAsTextFile(output)
    sc.stop()



""" DO NOT EDIT PAST THIS LINE

You are welcome to read through the following code, but you
do not need to worry about understanding it.
"""

def main():
    """
    Parses command line arguments and runs the solver appropriately.
    If nothing is passed in, the default values are used.
    """
    parser = argparse.ArgumentParser(
            description="Returns back the entire solution graph.")
    parser.add_argument("-M", "--master", type=str, default="local[8]",
            help="url of the master for this job")
    parser.add_argument("-O", "--output", type=str, default="solution-out",
            help="name of the output file")
    parser.add_argument("-H", "--height", type=int, default=2,
            help="height of the puzzle")
    parser.add_argument("-W", "--width", type=int, default=2,
            help="width of the puzzle")
    parser.add_argument("-S", "--slaves", type=int, default=6,
            help="number of slaves executing the job")
    args = parser.parse_args()

    global PARTITION_COUNT
    PARTITION_COUNT = args.slaves * 16

    # call the puzzle solver
    solve_puzzle(args.master, args.output, args.height, args.width, args.slaves)

# begin execution if we are running this file directly
if __name__ == "__main__":
    main()
