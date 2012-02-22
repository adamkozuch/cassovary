/*
 * Copyright 2012 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.twitter.cassovary.graph

import org.specs.Specification

class DirectedPathSpec extends Specification {

  "path of many nodes" should {
    val testPathIds = Array(10, 11, 12, 14, 15, 11, 14, 0)

    "length, append, exists, equals" in {
      val testPathNodes = testPathIds map { TestNode(_, Nil, Nil) }
      val path = DirectedPath.builder[Node]()
      path.append(testPathNodes(0))
      path.snapshot mustEqual DirectedPath(List(testPathNodes(0)))
      (1 until testPathIds.length - 2) foreach { indx =>
        val node = testPathNodes(indx)
        path.append(node)
        val snapshotted = path.snapshot
        snapshotted.length mustEqual (indx + 1)
        snapshotted.exists(node) mustEqual true
        snapshotted.exists(testPathNodes(indx - 1)) mustEqual true
        snapshotted.exists(testPathNodes(testPathNodes.length - 1)) mustEqual false
      }

      val path2 = DirectedPath.builder[Node]()
      path2.append(testPathNodes(0))
      (path.snapshot == path2.snapshot) mustEqual false

      path.clear()
      path.snapshot.length mustEqual 0
      path.append(testPathNodes(0)).append(testPathNodes(1))

      path2.clear()
      path2.append(testPathNodes(0))
      path2.append(testPathNodes(1))
      path2.snapshot mustEqual DirectedPath(List(testPathNodes(0), testPathNodes(1)))
      (path.snapshot == path2.snapshot) mustEqual true
    }

    "a path of ints" in {
      val path = DirectedPath.builder[Int]()
      path.append(testPathIds(0))
      path.snapshot mustEqual DirectedPath(List(testPathIds(0)))
      path.clear()
      path.snapshot.length mustEqual 0
      path.append(testPathIds(0)).append(testPathIds(1))
      path.snapshot.length mustEqual 2
      path.snapshot mustEqual DirectedPath(List(testPathIds(0), testPathIds(1)))
    }

  }
}
