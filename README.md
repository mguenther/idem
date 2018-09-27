# Idem

[![Build Status](https://travis-ci.org/mguenther/idem.svg?branch=master)](https://travis-ci.org/mguenther/idem.svg) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.mguenther.idem/idem-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.mguenther.idem/idem-core)

This repository hosts *Idem*, a Java library that implements a *decentralized*, *k-ordered* approach to ID generation using *Flake IDs*. Idem comes with a couple of building blocks that are used to implement a variety of Flake IDs with differing characteristics. Currently, Idem realizes Flake-64, Flake-128 and Flake-160 IDs.

## Why should I bother?

If you are looking for ways to generate unique IDs that exhibit certain properties and require no coordination between individual nodes in your cluster, then you probably hit one of the following problems:

* Generating technical identifiers for database entities hits a bottleneck, since your demand on the write-side of your application is too frequent for any database-centric approach to ID generation to keep up. You need to generate unique IDs and you need it done fast without any coordination required between individual nodes of your cluster.
* Generated IDs should have exhibit some loose form of ordering if they are sorted lexicographically.
* Application-level generated UUIDs of versions that incorporate time are of no use either, since they are susceptible to clock drift.
* Of course you can implement your own library, but have you thought about NTP updates messing with your local time source (e.g. time flows backwards due to adjustments for clock drift)?

Idem implements a fast and reliable way to generate Flake IDs that require no coordination between nodes in your cluster.

## Provided ID generators

Idem currently comes with the following ID generators equipped.

| Generator    | Width | Target Representation     | Comprises                                           |
| ------------ | ----- | ------------------------- | --------------------------------------------------- |
| `Flake64L`   | 64    | `Long`                    | 41-bit timestamp, 10-bit worker ID, 12-bit sequence |
| `Flake64S`   | 64    | `String` (Base62-encoded) | 41-bit timestamp, 10-bit worker ID, 12-bit sequence |
| `Flake128S`  | 128   | `String` (Base62-encoded) | 64-bit timestamp, 48-bit worker ID, 16-bit sequence |

## Using Idem

Using the individual Flake ID generators is quite easy. Have a look at the following example which demonstrates how to instantiate a Flake-64 generator that represents generated IDs as `java.lang.Long`-typed values.

```java
Flake64L flake64 = new Flake64L(
        timeProvider,
        new StaticWorkerIdProvider("ASvmcvljs=!"), // this worker ID has its LSB set to 1
        new LongEncoder());
Long generatedId = flake64.nextId();
```

## License

This work is released under the terms of the Apache 2.0 license.

<p>
    <div align="center">
        <div><img src="made-in-darmstadt.jpg"></div>
        <div><a href="https://mguenther.net">mguenther.net</a></div>
    </div>
</p>