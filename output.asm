li  $t0,4
li  $t1,3
li  $t2,5
li  $t3,25
li  $t4,52
li  $t5,3
mult  $t1,$t5,
mflo  $t5
sub  $t5,$t0,$t5,
move  $t1,$t5
li  $t5,3
div  $t0,$t5,
mflo  $t5
mult  $t5,$t1,
mflo  $t5
li  $t6,21
add  $t6,$t5,$t6,
li  $t5,2
mult  $t0,$t5,
mflo  $t5
sub  $t6,$t6,$t5,
mult  $t1,$t2,
mflo  $t5
add  $t6,$t6,$t5,
move  $t5,$t6
li  $t6,5
mult  $t5,$t6,
mflo  $t6
li  $t7,2
div  $t0,$t7,
mflo  $t7
mult  $t7,$t2,
mflo  $t7
add  $t6,$t6,$t7,
li  $t7,6
add  $t7,$t6,$t7,
li  $t6,7
sub  $t6,$t7,$t6,
move  $t7,$t6
move  $a0,$t5
li  $v0,1
syscall
move  $a0,$t7
li  $v0,1
syscall
