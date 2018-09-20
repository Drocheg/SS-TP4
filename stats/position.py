import numpy as np
import matplotlib.pyplot as plt
from math import exp, cos, sqrt

def analytic(t) :
    return exp(-50*t/70) * cos(t * sqrt(10000/70 - 2500/(70*70)))

def trajectory(dir, show = False):
    with open(f'{dir}/positions_beeman.dat') as f:
        beeman = f.readlines()

    with open(f'{dir}/positions_gear.dat') as f:
        gear = f.readlines()

    with open(f'{dir}/positions_verlet.dat') as f:
        verlet = f.readlines()


    x_b = []
    y_b = []

    x_g = []
    y_g = []

    x_v = []
    y_v = []

    for i in range(len(beeman)):
        x_b.append(float(beeman[i].split(' ')[0]))
        y_b.append(float(beeman[i].split(' ')[2]))

    for i in range(len(gear)):
        x_g.append(float(gear[i].split(' ')[0]))
        y_g.append(float(gear[i].split(' ')[2]))

    for i in range(len(verlet)):
        x_v.append(float(verlet[i].split(' ')[0]))
        y_v.append(float(verlet[i].split(' ')[2]))


    plt.style.use('ggplot')


    xs = [0.01 * x for x in range(int(max(x_b) / 0.01))]
    ys = [analytic(x) for x in xs]

    
    plt.plot(x_b, y_b, 'k')
    plt.plot(x_g, y_g, 'y')
    plt.plot(x_v, y_v, 'g')
    plt.plot(xs, ys)
    


    if show:
        plt.show()
    else:
       plt.savefig(f'images/{dir}/bigTrajectory.png', format='png', bbox_inches = 'tight', dpi = 100)
    plt.close()
    

trajectory('spring', True)