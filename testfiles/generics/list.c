
void array_int_init(array_int_t* this, uint64 capacity) {
    this->length = 0;
    this->capacity = capacity;
    this->ptr = malloc(sizeof(int) * capacity);
}

int main() {
    array_int_t* numbers = malloc(sizeof(array_int_t) * 10);
    array_int_init(numbers, 10);

}
