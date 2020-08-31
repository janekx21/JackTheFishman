package dodgyDeliveries3.prefabs

import dodgyDeliveries3.GameObject
import dodgyDeliveries3.components.EnemySpawner

fun makeEnemySpawner() : GameObject {
    return GameObject("Enemy Spawner").also { gameObject ->
        gameObject.addComponent<EnemySpawner>()
    }
}
