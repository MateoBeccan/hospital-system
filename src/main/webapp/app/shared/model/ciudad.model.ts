import { type IProvincia } from '@/shared/model/provincia.model';

export interface ICiudad {
  id?: number;
  nombre?: string;
  codigo?: string | null;
  codigoPostal?: string | null;
  fechaAlta?: Date;
  fechaBaja?: Date | null;
  activo?: boolean;
  provincia?: IProvincia;
}

export class Ciudad implements ICiudad {
  constructor(
    public id?: number,
    public nombre?: string,
    public codigo?: string | null,
    public codigoPostal?: string | null,
    public fechaAlta?: Date,
    public fechaBaja?: Date | null,
    public activo?: boolean,
    public provincia?: IProvincia,
  ) {
    this.activo = this.activo ?? false;
  }
}
