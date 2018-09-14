import numpy as np
import matplotlib.pyplot as plt
from math import exp, cos, sqrt

def trajectory(dir, show = False):
    with open(f'{dir}/positions_earth.dat') as f:
        verlet = f.readlines()

    x_v = []
    y_v = []

    x_sun = []
    y_sun = []

    x_jup = []
    y_jup = []

    for i in range(len(verlet)):
        x_v.append(float(verlet[i].split(' ')[2]))
        y_v.append(float(verlet[i].split(' ')[3]))
        x_sun.append(float(verlet[i].split(' ')[4]))
        y_sun.append(float(verlet[i].split(' ')[5]))
        x_jup.append(float(verlet[i].split(' ')[6]))
        y_jup.append(float(verlet[i].split(' ')[7]))


    plt.style.use('ggplot')

    plt.plot(x_jup, y_jup, 'k')
    plt.plot(x_v, y_v, 'g')
    plt.plot(x_sun, y_sun, 'r')

    print(min(x_jup))
    plt.xlim(-1e9, 1e9)
#    plt.ylim(min(y_jup) - 10000, max(y_jup) + 10000)

    if show:
        plt.show()
    else:
       plt.savefig(f'images/{dir}/bigTrajectory.png', format='png', bbox_inches = 'tight', dpi = 100)
    plt.close()
    

trajectory('planet', True)