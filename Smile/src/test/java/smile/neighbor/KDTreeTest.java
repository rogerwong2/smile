/*******************************************************************************
 * Copyright (c) 2010 Haifeng Li
 *   
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package smile.neighbor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import smile.math.Math;
import smile.math.distance.EuclideanDistance;

/**
 *
 * @author Haifeng Li
 */
public class KDTreeTest {

    double[][] data = null;
    KDTree<double[]> kdtree = null;
    LinearSearch<double[]> naive = null;

    public KDTreeTest() {
        data = new double[10000][];
        for (int i = 0; i < data.length; i++) {
            data[i] = new double[10];
            for (int j = 0; j < data[i].length; j++)
                data[i][j] = Math.random();
        }

        kdtree = new KDTree<double[]>(data, data);
        naive = new LinearSearch<double[]>(data, new EuclideanDistance());
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of nearest method, of class KDTree.
     */
    @Test
    public void testNearest() {
        System.out.println("nearest");
        for (int i = 0; i < data.length; i++) {
            Neighbor<double[], double[]> n1 = kdtree.nearest(data[i]);
            Neighbor<double[], double[]> n2 = naive.nearest(data[i]);
            assertEquals(n1.index, n2.index);
            assertEquals(n1.value, n2.value);
            assertEquals(n1.distance, n2.distance, 1E-7);
        }
    }

    /**
     * Test of knn method, of class KDTree.
     */
    @Test
    public void testKnn() {
        System.out.println("knn");
        for (int i = 0; i < data.length; i++) {
            Neighbor<double[], double[]> [] n1 = kdtree.knn(data[i], 10);
            Neighbor<double[], double[]> [] n2 = naive.knn(data[i], 10);
            for (int j = 0; j < n1.length; j++) {
                assertEquals(n1[j].index, n2[j].index);
                assertEquals(n1[j].value, n2[j].value);
                assertEquals(n1[j].distance, n2[j].distance, 1E-7);
            }
        }
    }

    /**
     * Test of range method, of class KDTree.
     */
    @Test
    public void testRange() {
        System.out.println("range");
        List<Neighbor<double[], double[]>> n1 = new ArrayList<Neighbor<double[], double[]>>();
        List<Neighbor<double[], double[]>> n2 = new ArrayList<Neighbor<double[], double[]>>();
        for (int i = 0; i < data.length; i++) {
            kdtree.range(data[i], 0.5, n1);
            naive.range(data[i], 0.5, n2);
            Collections.sort(n1);
            Collections.sort(n2);
            assertEquals(n1.size(), n2.size());
            for (int j = 0; j < n1.size(); j++) {
                assertEquals(n1.get(j).index, n2.get(j).index);
                assertEquals(n1.get(j).value, n2.get(j).value);
                assertEquals(n1.get(j).distance, n2.get(j).distance, 1E-7);
            }
            n1.clear();
            n2.clear();
        }
    }
}