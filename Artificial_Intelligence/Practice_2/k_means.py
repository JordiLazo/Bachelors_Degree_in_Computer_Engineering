#! /usr/bin/env python3
# -*- coding: utf-8 -*-

import random as r

import matplotlib.pyplot as plt

from treepredict import read_file


class Kmeans:
    """ K means class model"""

    def __init__(self, k, distance, max_iters, use_range=True):
        self.k = k
        self.distance = distance
        self.use_range = use_range
        self.max_iters = max_iters
        self.inertia_ = float(0)
        # use_range : select random value in the range(True) or select ponts as centroids...
        self.centroids = list()

    @staticmethod
    def _get_range_random_value(points, feature_idx):
        """
        Get a random value inside the feature range. The values
        where this range is extracted for are the ones present
        in the points data.
        """
        feat_values = [point[feature_idx] for point in points]
        feat_max = max(feat_values)
        feat_min = min(feat_values)
        return r.random() * (feat_max - feat_min) + feat_min

    def _create_random_centroids(self, points):
        """Random centroids in the range of the points"""
        n_feats = len(points[0])
        for _ in range(self.k):
            point = [0.0] * n_feats
            for feature_idx in range(n_feats):
                point[feature_idx] = self._get_range_random_value(points, feature_idx)
            self.centroids.append(point)

    def _create_points_centroids(self, points):
        """Random points selected as centrodis"""
        self.centroids = []
        while True:
            if len(self.centroids) == self.k:
                break
            generated_idx = r.randint(0, len(points) - 1)
            centroid = points[generated_idx]
            if centroid not in self.centroids:
                self.centroids.append(centroid)

    def _get_closest_centroid(self, row):
        """
        point -> calculate distance
        """
        min_dist = 2 ** 64
        closest = None
        for centroid_idx, centroid in enumerate(self.centroids):
            dist = self.distance(row, centroid)
            if dist < min_dist:
                min_dist = dist
                closest = centroid_idx
        return closest

    @staticmethod
    def _average_points(points):
        n_points = len(points)
        n_feats = len(points[0])
        avrg = [0.0] * n_feats
        # (1,2),(2,3),(44,2)
        for feat in range(n_feats):
            feat_values = [point[feat] for point in points]
            avrg[feat] = sum(feat_values) / n_points
        return avrg

    def _update_centroids(self, bestmatches, rows):
        # bestmatches = [[1,2,3],[4,5],[6]]
        for centroid_idx in range(self.k):
            points = [rows[point_idx] for point_idx in bestmatches[centroid_idx]]
            if not points:
                continue
            avrg = self._average_points(points)
            self.centroids[centroid_idx] = avrg

    def fit(self, rows, restarting_policies=1):
        """
        Fit the model
        """
        inertia_min_ = float(0)
        lastmatches_best = None

        for _ in range(restarting_policies):
            self.centroids = list()
            # 1. Set the k centroids randomly
            if self.use_range:
                self._create_random_centroids(rows)
            else:
                self._create_points_centroids(rows)

            lastmatches = None
            for iteration in range(self.max_iters):
                bestmatches = [[] for _ in range(self.k)]
                for row_idx, row in enumerate(rows):
                    centroid = self._get_closest_centroid(row)
                    bestmatches[centroid].append(row_idx)

                # end/stop/goal condition
                if bestmatches == lastmatches:
                    break

                lastmatches = bestmatches
                self._update_centroids(bestmatches, rows)

            inertia_ = self._calculate_inertia(lastmatches, rows)

            if inertia_ > inertia_min_:
                inertia_min_ = inertia_
                lastmatches_best = lastmatches

        self.inertia_ = inertia_min_
        return lastmatches_best

    def predict(self, rows):
        """
        Predict the closest cluster each sample in rows belong to.
        what does map(self._get_closest_centroid,rows)
        """
        return list(map(self._get_closest_centroid, rows))

    def _calculate_inertia(self, data_indexes, rows):
        intracentroid_dst = []
        i = 0
        for centroid in self.centroids:
            intracentroid_dst.append(self._intracentroid_distance(centroid, data_indexes[i], rows))
            i += 1

        return sum(intracentroid_dst)

    def _intracentroid_distance(self, centroid, indexes_assigned, rows):
        i_result = float(0)
        for j in indexes_assigned:
            i_result += self.distance(rows[j], centroid)
        return i_result


def euclidean_squared(point1, point2):
    """ euclidean_squared distance

        Parameters:
        point1 (int,int): (X0,Y0)
        point2 (int,int): (X,Y)

        Returns:
        int: (X-X0)^2+(Y-Y0)^2

       """
    return sum(
        (point1 - point2) ** 2
        for point1, point2 in zip(point1, point2)
    )


def total_distance_function(rows, k_inicial=1, k_final=10, use_range=True, restarting_policies=1):
    """ Creates a table a relation with inertia and cluster """
    inertias = []

    print("Inertias between Clusters K0=", k_inicial, ", K=", k_final)
    print("K\tInertia")
    range_k = range(k_inicial, k_final + 1)
    for k in range_k:
        kmeans_test = Kmeans(k=k, distance=euclidean_squared, use_range=use_range, max_iters=100)
        kmeans_test.fit(rows, restarting_policies)
        print(k, "\t", kmeans_test.inertia_)
        inertias.append(kmeans_test.inertia_)
    plt.plot(range_k, inertias, 'bx-')
    plt.xlabel('Values of K')
    plt.ylabel('Inertia')
    plt.title('The Elbow Method using Inertia')
    plt.show()


if __name__ == '__main__':
    data = read_file("seeds.csv", ",")
    total_distance_function(data, k_inicial=1, k_final=10, restarting_policies=10)

    # data = [[2, 4],[3, 5],[3, 2],[5, 2],[5, 4],[7, 3],[7, 8],[8, 4]]
    # kmeans = Kmeans(k=9, distance=euclidean_squared,use_range=True, max_iters=1000)
    # bestmatches = kmeans.fit(data,restarting_policies=2)

    # print("bestmatches", bestmatches)
    # print("Centroids: ", kmeans.centroids)
    # print("Predictions", kmeans.predict(data))
    # print("inertia = ",kmeans.inertia_)
