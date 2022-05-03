import java.util.ArrayList;
import java.util.HashSet;

public class ContactFinder {
    // todo for students
    // Returns a HashSet of ContactResult objects representing all the contacts between circles in the scene.
    // The runtime of this method should be O(n^2) where n is the number of circles.
    public static HashSet<ContactResult> getContactsNaive(ArrayList<Circle> circles) {
        HashSet<ContactResult> h_set = new HashSet<ContactResult>();
        for (int i = 0; i < circles.size(); i++) {
            for (int j = i + 1; j < circles.size(); j++) {
                if (circles.get(i).isContacting(circles.get(j)) != null && circles.get(i).id != circles.get(j).id) {
                    h_set.add(circles.get(i).isContacting(circles.get(j)));
                }
            }
        }
        return h_set;
    }

    // todo for students
    // Returns a HashSet of ContactResult objects representing all the contacts between circles in the scene.
    // The runtime of this method should be O(n*log(n)) where n is the number of circles.
    public static HashSet<ContactResult> getContactsBVH(ArrayList<Circle> circles, BVH bvh) {
        HashSet<ContactResult> h_set1 = new HashSet<ContactResult>();
        for (Circle i : circles) {
            for (ContactResult j : getContactBVH(i, bvh)) {
                h_set1.add(j);
            }

        }
        return h_set1;

    }

    // todo for students
    // Takes a single circle c and a BVH bvh.
    // Returns a HashSet of ContactResult objects representing contacts between c
    // and the circles contained in the leaves of the bvh.
    public static HashSet<ContactResult> getContactBVH(Circle c, BVH bvh) {
        HashSet<ContactResult> h_set = new HashSet<ContactResult>();
        if (!(c.getBoundingBox().intersectBox(bvh.boundingBox))) {
            return h_set;
        } else {
            if (bvh.child1 == null && bvh.child2 == null && bvh.containedCircle!=null) {
                if ( c.id != bvh.containedCircle.id) {
                    if(c.isContacting(bvh.containedCircle)!=null)
                    h_set.add(c.isContacting(bvh.containedCircle));
                    return h_set;
                }
            } else if (bvh.child1 != null && bvh.child2 != null && bvh.containedCircle==null)  {

                HashSet<ContactResult> childL=getContactBVH(c, bvh.child1);
                HashSet<ContactResult> childR=getContactBVH(c, bvh.child2);
                childL.addAll(childR);
                return childL;
            }
        }

        return h_set;
    }

}
