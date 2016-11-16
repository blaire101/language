#!/bin/bash

echo "start deploy ..."
git status
git commit -m "add a article or modify article"
git push origin master
echo "successful commit !"
