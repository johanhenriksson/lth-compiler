; Library Functions
declare i8* @__malloc(i32)
declare void @__delete(i8*)
declare void @print(i64)
declare i64 @read()
declare i64 @truncate(double)
declare double @promote(i64)
declare void @assert(i1)
declare void @except(i1)

define i64 @main() {
ret i64 0
}

