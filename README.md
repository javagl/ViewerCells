# ViewerCells

A viewer and renderer for rectangle and hexagon cell maps

![ViewerCellsScreenshot01.png](/screenshots/ViewerCellsScreenshot01.png)

The `CellMapPanel` is a [Viewer](https://github.com/javagl/Viewer)
for maps of rectangular or [Hexagon](https://github.com/javagl/Hexagon)
cells. The maps may be zoomed, translated and rotated. Custom painting
operations may be performed for the cells. 
The [BasicCellPainter](https://github.com/javagl/ViewerCells/blob/master/src/main/java/de/javagl/viewer/cells/BasicCellPainter.java)
class is offers an easy way to configure how the cells are painted,
and to configure the border- and fill colors for the cell shapes,
labels and fonts. Additionally, it may serve as a base class for own
implementations of cell painters.

The basic usage is demonstrated in the 
[ViewerCellsTest](https://github.com/javagl/ViewerCells/blob/master/src/test/java/de/javagl/viewer/cells/test/ViewerCellsTest.java)
class.
