import numpy as np
import matplotlib.pyplot as plt
from math import exp, cos, sqrt, log

def analytic(t) :
    return exp(-50*t/70) * cos(t * sqrt(10000/70 - 2500/(70*70)))

def trajectory(dir, show = False):
    dt = []
    msd_beeman = []
    msd_verlet = []
    msd_gear = []
    
    for i in range(1,101):
        with open(f'{dir}/positions_beeman_{i}.dat') as f:
            beeman = f.readlines()

        with open(f'{dir}/positions_gear_{i}.dat') as f:
            gear = f.readlines()

        with open(f'{dir}/positions_verlet_{i}.dat') as f:
            verlet = f.readlines()

        dt.append(float(beeman[1].split(' ')[0])) # La segunda linea tiene dt en la primera columna
        
        ts = []
        beemans = []
        gears = []
        verlets = []
        

        for i in range(len(beeman)):
            ts.append(float(beeman[i].split(' ')[0]))
            beemans.append(float(beeman[i].split(' ')[2]))

        for i in range(len(gear)):
            gears.append(float(gear[i].split(' ')[2]))

        for i in range(len(verlet)):
            verlets.append(float(verlet[i].split(' ')[2]))

        # Calculo errores cuadraticos
        err_b = []
        for i in range(len(beemans)):
            t = ts[i]
            x = beemans[i]
            truex = analytic(t)
            err_b.append((x - truex)**2)
       
        err_g = []
        for i in range(len(gears)):
            t = ts[i]
            x = gears[i]
            truex = analytic(t)
            err_g.append((x - truex)**2)
       
        err_v = []
        for i in range(len(verlets)):
            t = ts[i]
            x = verlets[i]
            truex = analytic(t)
            err_v.append((x - truex)**2)

        msd_beeman.append(np.mean(err_b))
        msd_verlet.append(np.mean(err_v))
        msd_gear.append(np.mean(err_g))


    plt.style.use('ggplot')

    verletLine = plt.loglog(dt, msd_verlet, 'g',  label = 'Verlet')
    beemanLine = plt.loglog(dt, msd_beeman, 'k', label = 'Beeman')
    gearLine = plt.loglog(dt, msd_gear, 'y',  label = 'Gear')

    plt.legend(
        loc='best',
        fontsize=15)

    
    print(log(msd_beeman[1]/msd_beeman[50])/log(dt[1]/dt[50]))
    print(log(msd_gear[1]/msd_gear[50])/log(dt[1]/dt[50]))
    print(log(msd_verlet[1]/msd_verlet[50])/log(dt[1]/dt[50]))

    if show:
        plt.show()
    else:
       plt.savefig(f'images/{dir}/bigTrajectory.png', format='png', bbox_inches = 'tight', dpi = 100)
    plt.close()
    

trajectory('errors_out', True)