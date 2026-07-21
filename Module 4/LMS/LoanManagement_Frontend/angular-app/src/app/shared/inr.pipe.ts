import { Pipe, PipeTransform } from '@angular/core';

/** Formats a number as Indian Rupees, e.g. 400000 -> ₹4,00,000. */
@Pipe({ name: 'inr', standalone: true })
export class InrPipe implements PipeTransform {
  transform(value: number | null | undefined, fractionDigits = 0): string {
    if (value == null || isNaN(value)) return '₹0';
    return (
      '₹' +
      Number(value).toLocaleString('en-IN', {
        minimumFractionDigits: fractionDigits,
        maximumFractionDigits: fractionDigits,
      })
    );
  }
}
