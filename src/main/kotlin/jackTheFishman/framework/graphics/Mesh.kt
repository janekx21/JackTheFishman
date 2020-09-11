package jackTheFishman.framework.graphics

import com.beust.klaxon.TypeFor
import jackTheFishman.framework.math.times
import jackTheFishman.framework.math.toMatrix4fc
import jackTheFishman.framework.math.toVector3fc
import jackTheFishman.framework.util.ICreateViaPath
import jackTheFishman.framework.util.IDrawable
import jackTheFishman.framework.util.IUsable
import jackTheFishman.framework.util.typeAdapter.MeshTypeAdapter
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.assimp.AIMesh
import org.lwjgl.assimp.AINode
import org.lwjgl.assimp.AIScene
import org.lwjgl.assimp.Assimp
import org.lwjgl.opengl.GL46.*

@TypeFor(field = "type", adapter = MeshTypeAdapter::class)
open class Mesh(private val data: Array<Vertex>) : IDrawable, IUsable {
    val type: String = javaClass.simpleName

    private val vbo: Int = glGenBuffers()
    private val vao: Int = glGenVertexArrays()

    init {
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        val buffer = data.flatMap { vertex -> vertex.asList() }.toFloatArray()
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW)

        glBindVertexArray(vao)
        Vertex.generateVertexAttributePointers()
        glBindVertexArray(0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
    }

    override fun use(callback: () -> Unit) {
        glBindVertexArray(vao)
        callback()
        glBindVertexArray(0)
    }

    override fun draw() {
        this.use {
            glDrawArrays(GL_TRIANGLES, 0, data.size)
        }
    }

    companion object : ICreateViaPath<Mesh> {
        fun getMeshDataViaPath(path: String): Array<Vertex> {
            val scene =
                Assimp.aiImportFile(
                    path,
                    Assimp.aiProcess_Triangulate or Assimp.aiProcess_GenNormals or Assimp.aiProcess_CalcTangentSpace
                )
            check(scene != null) { Assimp.aiGetErrorString()!! }

            val root = scene.mRootNode()!!
            val list = processMesh(scene, root)
            return list.toTypedArray()
        }

        override fun createViaPath(path: String): Mesh {
            return MeshViaPath(path)
        }

        private val constantScale = Matrix4f().scale(.01f)

        private fun nodeToVertices(scene: AIScene, node: AINode): ArrayList<Vertex> {
            val list = arrayListOf<Vertex>()
            val mat = node.mTransformation().toMatrix4fc() * constantScale

            for (i in 0 until node.mNumMeshes()) {
                val meshIndex = node.mMeshes()?.get(i)!!
                val meshPointer = scene.mMeshes()!![meshIndex]
                if (meshPointer != 0L) {
                    val mesh = AIMesh.create(meshPointer)

                    for (j in 0 until mesh.mNumFaces()) {
                        val face = mesh.mFaces().get(j)
                        for (k in 0 until face.mNumIndices()) {
                            val index = face.mIndices().get(k)
                            val vec = mesh.mVertices().get(index)
                            val uv = mesh.mTextureCoords(0)?.get(index)
                            check(uv != null) { "no uv provided" }
                            val normal = mesh.mNormals()?.get(index)
                            val tangent = mesh.mTangents()?.get(index)
                            check(normal != null) { "no normal provided" }
                            check(tangent != null) { "no tangent provided" }
                            val vertex = Vertex(
                                Vector3f(vec.toVector3fc()).mulTransposePosition(mat),
                                Vector2f(uv.x(), uv.y()),
                                Vector3f(normal.toVector3fc()).mulTransposeDirection(mat),
                                Vector3f(tangent.toVector3fc()).mulTransposeDirection(mat)
                            )
                            list.add(vertex)
                        }
                    }
                }
            }
            return list
        }

        private fun processMesh(
            scene: AIScene,
            parent: AINode
        ): ArrayList<Vertex> {
            val list = nodeToVertices(scene, parent)
            for (w in 0 until parent.mNumChildren()) {
                val address = parent.mChildren()?.get(w)!!
                val node = AINode.create(address)
                list += processMesh(scene, node)
            }
            return list
        }
    }
}
