# Versioning
Project uses semantic version. Read more at https://semver.org

# Development

### Git strategy
Project uses github flow with the exception to branch naming. Read more at https://githubflow.github.io

### Naming
Each issue has an appropriate number. This number must be used with prefix 'ISS-${issue-number} in **branch** AND **commits** for that issue.\
Example:

    As i started working on versioning issue with number 3, i have created a branch ISS-3 then commit my changes with 'ISS-3' prefix to message. 

#Release
Release is performed by a maintainer.\
To perform a release:

    mvn -B com.amashchenko.maven.plugin:gitflow-maven-plugin:release  -DreleaseVersion=${releaseVersion}
