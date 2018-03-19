[![Build Status](https://img.shields.io/travis/NOVA-Team/NOVA-Core/master.svg?style=flat-square)](https://travis-ci.org/NOVA-Team/NOVA-Core)
[![Coverage](https://img.shields.io/codecov/c/github/NOVA-Team/NOVA-Core.svg?style=flat-square)](https://codecov.io/github/NOVA-Team/NOVA-Core)
[![Tests](https://img.shields.io/jenkins/t/http/ci.novaapi.net/NOVA-Core.svg?style=flat-square)](http://ci.novaapi.net/job/NOVA-Core/lastCompletedBuild/testReport/)
[![waffle.io](https://img.shields.io/badge/tracker-waffle.io-blue.svg?style=flat-square)](https://waffle.io/NOVA-Team/NOVA-Core)

Nova API
========
NOVA is a voxel game modding framework designed to allow mods to be run across different voxel games.

NOVA is licensed under the LGPL v3 License.
http://opensource.org/licenses/lgpl-3.0.html

### Important links
* Issue tracker [GitHub](https://github.com/NOVA-Team/NOVA-Core/issues)
* Project main page (http://novaapi.net)

### Dependencies
For all libraries we use, include it in your IDE as a jar library.

* Silk Dependency Injection (http://search.maven.org/remotecontent?filepath=se/jbee/silk-di/0.6/silk-di-0.6.jar
* Guava (http://search.maven.org/remotecontent?filepath=com/google/guava/guava/18.0/guava-18.0.jar)
* JUnit (http://search.maven.org/remotecontent?filepath=junit/junit/4.12/junit-4.12.jar)
* AssertJ

Formatting
----------
Any contributions should use the NOVA formatter. Instructions for applying to IDEA and Eclipse formatters can be found [here](http://novaapi.net/docs/NOVA%20Development/Formatting/).

Removing merge commits
----------------------
Merge commits happen when you `git pull` with local commits. An example is [here](http://git.io/bseX).
These can be avoided by running `git pull --rebase`. Of course you don't want to do this every time:

In the NOVAAPI repo (and any other repos where you want no merge commits)
```
git config branch.master.rebase true
```

To set this up automagically in any __newly cloned__ repos:

```
git config --global branch.autosetuprebase always
```
