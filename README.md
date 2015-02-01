Nova API
========
NOVA is a voxel game modding framework designed to allow mods to be run across different voxel games.

NOVA is licensed under the LGPL v3 License.
http://opensource.org/licenses/lgpl-3.0.html

###Build
[![Build Status](https://travis-ci.org/NOVAAPI/NovaCore.svg?branch=master)](https://travis-ci.org/NOVAAPI/NovaCore)

### Dependencies
* Guice
* Guava
* JUnit
* AssertJ

Using the IDEA formatter
------------------------
To use the formatter you find [here](https://github.com/NOVAAPI/NovaCore/tree/master/guidelines),
start IntelliJ IDEA, go to `Files->Import Settings...`,
select `guidelines/intelliJ-formatter.jar` and click `OK`.

Removing merge commits
----------------------
Merge commits happen when you `git pull` with local commits.
These can be avoided by running `git pull --rebase`. Of course you don't want to do this every time:

In the NOVAAPI repo (and any other repos where you want no merge commits)
```
git config branch.master.rebase true
```

To set this up automagically in any __newly cloned__ repos:
```
git config --global branch.autosetuprebase always
```
