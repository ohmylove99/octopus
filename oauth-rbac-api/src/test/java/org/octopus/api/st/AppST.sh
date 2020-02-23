rm -f out.json
curl -vso out.json http://localhost:8080/api/app/health
if [ -n $(cat out.json | grep "UP") ] ; then
    echo "Smoke test Ok."
fi