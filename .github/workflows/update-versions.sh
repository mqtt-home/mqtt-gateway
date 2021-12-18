#!/bin/bash
set -e
cd "$(dirname "$0")"
VERSION=$(gh release list | grep Latest | awk '{print $1}')
echo "Update to version $VERSION"
mkdir build
pushd build
  repos=PROJECTS
  for REPO in "${repos[@]}"
  do
    gh repo clone "mqtt-home/$REPO"
      pushd "$REPO/src"
        git checkout -b "mqttgateway-$VERSION"
        mvn versions:use-dep-version \
          -DdepVersion="$VERSION" \
          -Dincludes=de.rnd7.mqttgateway \
          -DforceVersion=true \
          -DprocessDependencies=true \
          -DgenerateBackupPoms=false

        git add .
        git commit -m"Update to mqttgateway $VERSION"
        git push --set-upstream origin "mqttgateway-$VERSION"
        gh pr create --title "Dependency: Update to mqttgateway $VERSION" --body "Dependency: Update to mqttgateway $VERSION"
      popd
  done
popd
