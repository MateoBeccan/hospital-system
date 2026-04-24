import { beforeEach, describe, expect, it } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';
import sinon from 'sinon';

import { DATE_FORMAT } from '@/shared/composables/date-format';
import { Especialidad } from '@/shared/model/especialidad.model';

import EspecialidadService from './especialidad.service';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('Especialidad Service', () => {
    let service: EspecialidadService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new EspecialidadService();
      currentDate = new Date();
      elemDefault = new Especialidad(123, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', currentDate, currentDate, false);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          fechaAlta: dayjs(currentDate).format(DATE_FORMAT),
          fechaBaja: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a Especialidad', async () => {
        const returnedFromService = {
          id: 123,
          fechaAlta: dayjs(currentDate).format(DATE_FORMAT),
          fechaBaja: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        const expected = { fechaAlta: currentDate, fechaBaja: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Especialidad', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Especialidad', async () => {
        const returnedFromService = {
          codigo: 'BBBBBB',
          nombre: 'BBBBBB',
          descripcion: 'BBBBBB',
          fechaAlta: dayjs(currentDate).format(DATE_FORMAT),
          fechaBaja: dayjs(currentDate).format(DATE_FORMAT),
          activa: true,
          ...elemDefault,
        };

        const expected = { fechaAlta: currentDate, fechaBaja: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Especialidad', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Especialidad', async () => {
        const patchObject = {
          codigo: 'BBBBBB',
          nombre: 'BBBBBB',
          fechaAlta: dayjs(currentDate).format(DATE_FORMAT),
          fechaBaja: dayjs(currentDate).format(DATE_FORMAT),
          ...new Especialidad(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { fechaAlta: currentDate, fechaBaja: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Especialidad', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Especialidad', async () => {
        const returnedFromService = {
          codigo: 'BBBBBB',
          nombre: 'BBBBBB',
          descripcion: 'BBBBBB',
          fechaAlta: dayjs(currentDate).format(DATE_FORMAT),
          fechaBaja: dayjs(currentDate).format(DATE_FORMAT),
          activa: true,
          ...elemDefault,
        };
        const expected = { fechaAlta: currentDate, fechaBaja: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Especialidad', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Especialidad', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Especialidad', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
