for f in $(find -name "*.java")
do
    cat license-short.txt $f > $f.new
    mv $f.new $f
done
