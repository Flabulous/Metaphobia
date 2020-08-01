Print("Metaphobia v0.0.2 - Deep Winter Studios")
Input("Press enter to start.")
Graphics3D 1920,1080,32,1
HidePointer 
SetBuffer BackBuffer()
AmbientLight 0,0,0
Print("Loading...")


Include "player.bb"
Include "cell.bb"

SeedRnd MilliSecs()

;GLOBAL VARIABLES
Global player = CreatePivot()
Global camera = CreateCamera(player)
Global flashlight = CreateLight(2,player)

Global pcx%
Global pcy%
Global pmn,pme,pms,pmw

Global PLAY_COLL = 2

Const scale_x# = 2.7
Const scale_y# = 2.7
Const scale_z# = 0.1

Const map_size_x = 512
Const map_size_y = 512

Const max_draw_x = 12
Const max_draw_y = 12

;DEBUG STUFF
Global CompassTex = LoadTexture("Textures/compass.bmp")
Global NWT = LoadTexture("Textures/northwall.bmp")
Global SWT = LoadTexture("Textures/southwall.bmp")
Global EWT = LoadTexture("Textures/eastwall.bmp")
Global WWT = LoadTexture("Textures/westwall.bmp")

Global Cell Dim mainmap.Cell(map_size_x+1,map_size_y+1) ;Maze array

Function LoadChunk()
	
	minx = pcx-max_draw_x
	miny = pcy-max_draw_y

	maxx = pcx+max_draw_x
	maxy = pcy+max_draw_y
	
	For i = minx To maxx Step 1
		For j = miny To maxy Step 1
			If i > 0 And j > 0
				If mainmap(i,j) = Null Then mainmap(i,j) = RndCell(i,j,Rnd(1,4))
				ShowCell(mainmap(i,j))
			EndIf	
		Next
	Next

End Function

Function DeloadChunk()
	minx = pcx-max_draw_x
	miny = pcy-max_draw_y

	maxx = pcx+max_draw_x
	maxy = pcy+max_draw_y
	
	For i = 0 To map_size_x Step 1
		For j = 0 To map_size_y Step 1

			If i > 0 And j > 0
			If i < minx And mainmap(i,j) <> Null
			 	HideCell(mainmap(i,j))
				mainmap(i,j)\is = 0
				Delete(mainmap(i,j))
			EndIf
			If j < miny And mainmap(i,j) <> Null
			 	HideCell(mainmap(i,j))
				mainmap(i,j)\is = 0
				Delete(mainmap(i,j))
			EndIf
			If i > maxx And mainmap(i,j) <> Null
			 	HideCell(mainmap(i,j))
				mainmap(i,j)\is = 0
				Delete(mainmap(i,j))
			EndIf
			If j > maxy And mainmap(i,j) <> Null
			 	HideCell(mainmap(i,j))
				mainmap(i,j)\is = 0
				Delete(mainmap(i,j))
			EndIf
			EndIf
		Next
	Next

End Function

Function UpdatePlayerCellPosition()
	ux% = Floor(EntityX(player) / 5.4)
	uy% = Floor(EntityZ(player) / 5.4)

	pcx = ux
	pcy = uy
End Function



LightRange flashlight,6
LightConeAngles flashlight,0,80
EntityType player,PLAY_COLL

Global compass = CreateCube()
RotateEntity compass,90,0,0
EntityTexture compass,CompassTex
ScaleEntity compass,2.7,2.7,0.1
PositionEntity(compass,-12,0,-12)

PositionEntity player,1382+5.4,3,1382+5.4
UpdatePlayerCellPosition()

LoadChunk()
DeloadChunk()
Collisions(PLAY_COLL,WALL_COLL,2,2)
;Input("Loading completed.")

While Not KeyHit(1)
	UpdatePlayerCellPosition()
	LoadChunk()
	DeloadChunk()
	
	TurnCamera(camera,player,0.1)
	ControlPlayer(player)

	UpdateWorld
	RenderWorld
	Flip

Wend
End