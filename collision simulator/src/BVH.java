import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BVH implements Iterable<Circle>{
    Box boundingBox;

    BVH child1;
    BVH child2;

    Circle containedCircle;

    // todo for students
    public BVH(ArrayList<Circle> circles) {
         this.boundingBox=buildTightBoundingBox(circles);
         if(circles.size()>1){
             this.child1 = new BVH(split(circles,boundingBox)[0]);
             this.child2 = new BVH(split(circles,boundingBox)[1]);
         }
         else if(circles.size()==1){
             this.containedCircle=circles.get(0);
         }
    }

    public void draw(Graphics2D g2) {
        this.boundingBox.draw(g2);
        if (this.child1 != null) {
            this.child1.draw(g2);
        }
        if (this.child2 != null) {
            this.child2.draw(g2);
        }
    }

    // todo for students
    public static ArrayList<Circle>[] split(ArrayList<Circle> circles, Box boundingBox) {
        ArrayList<Circle> LeftChildren = new ArrayList<Circle>();
        ArrayList<Circle> RightChildren = new ArrayList<Circle>();
        ArrayList<Circle>[] arrCircles = new ArrayList[2];

        //if(boundingBox==null){return null;}
        if(boundingBox.getHeight()>=boundingBox.getWidth()){
            double mid_y=boundingBox.getMidY();
            for(int i=0;i<circles.size();i++){
                double vy=circles.get(i).getBoundingBox().getMidY();
                if(vy>=mid_y){
                    RightChildren.add(circles.get(i));
                }else{
                    LeftChildren.add(circles.get(i));
                }
            }
        }else {
            double mid_x=boundingBox.getMidX();
            for(int j=0;j<circles.size();j++){
                double vx=circles.get(j).getBoundingBox().getMidX();
                if(vx>=mid_x){
                    RightChildren.add(circles.get(j));
                }else{
                    LeftChildren.add(circles.get(j));
                }
            }
        }

        arrCircles[0]=LeftChildren;
        arrCircles[1]=RightChildren;
        return arrCircles;
    }

    // returns the smallest possible box which fully encloses every circle in circles
    public static Box buildTightBoundingBox(ArrayList<Circle> circles) {
        Vector2 bottomLeft = new Vector2(Float.POSITIVE_INFINITY);
        Vector2 topRight = new Vector2(Float.NEGATIVE_INFINITY);

        for (Circle c : circles) {
            bottomLeft = Vector2.min(bottomLeft, c.getBoundingBox().bottomLeft);
            topRight = Vector2.max(topRight, c.getBoundingBox().topRight);
        }

        return new Box(bottomLeft, topRight);
    }

    // METHODS BELOW RELATED TO ITERATOR

    // todo for students
    @Override
    public Iterator<Circle> iterator() {

        return new BVHIterator(this);
    }

    public class BVHIterator implements Iterator<Circle> {
        int cur=0;
        ArrayList<Circle> root = new ArrayList<Circle>();
        // todo for students
        public BVHIterator(BVH bvh) {
        trv(bvh);
        }
        public void trv(BVH bvh){
            if(bvh.child1==null&&bvh.child2==null&&bvh.containedCircle!=null){
                root.add(bvh.containedCircle);
            }else if(bvh.child1!=null&&bvh.child2!=null&&bvh.containedCircle==null){
                trv(bvh.child2);
                trv(bvh.child1);
            }
        }
        // todo for students
        @Override
        public boolean hasNext() {
            return cur!= root.size();
        }

        // todo for students
        @Override
        public Circle next() {
            if(cur> root.size()-1){
            return null;
        }else{
            return root.get(cur++);}
        }
    }
}