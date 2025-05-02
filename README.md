# OS Project 2
Members: Mumo, Ethan, Jainam, Chioma

The Java portion of the project focuses on importing io, util, concurrent, and locks, in order to create a process class.

This public process regulates the pid, arrival time, burst time, and priority. 

# Producer Class
This class extends the thread by creating a private list of processes and a private buffer.
    Note: If there are too many processes, the buffer wo;; simulate arrival and catch an interrupted exception.
The buffer is shared by a Producer which can throw an exception.

# Consumer Class
This class also extends the same thread, creating it's own private buffer.
It will report the processes with their given names.

# Overview
The parser is in 4 parts, a pid, an arrival, a burst, and a priority. 
The priority, being the last process, catches exceptions and prints a stack containing the number of individual exceptions.
As the buffer is always running, it gives the consumer class more time to finish processing data. 
