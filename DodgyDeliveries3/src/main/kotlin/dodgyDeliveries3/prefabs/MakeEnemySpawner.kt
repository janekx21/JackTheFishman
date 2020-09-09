package dodgyDeliveries3.prefabs

import dodgyDeliveries3.GameObject
import dodgyDeliveries3.components.EnemySpawner

fun makeHardEnemySpawner() : GameObject {
    return GameObject("Enemy Spawner").also { gameObject ->
        gameObject.addComponent<EnemySpawner>().also {
            it.spawnInterval = 3f
        }
    }
}

fun makeEasyEnemySpawner() : GameObject {
    return GameObject("Enemy Spawner").also { gameObject ->
        gameObject.addComponent<EnemySpawner>().also {
            it.spawnInterval = 6f
        }
    }
}
